package pico.erp.work.schedule;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.event.EventPublisher;
import pico.erp.work.schedule.WorkScheduleRequests.CalculateEndRequest;
import pico.erp.work.schedule.category.WorkScheduleCategory;
import pico.erp.work.schedule.category.WorkScheduleCategoryId;
import pico.erp.work.schedule.category.WorkScheduleCategoryRepository;
import pico.erp.work.schedule.provider.WorkScheduleProvider;
import pico.erp.work.schedule.provider.WorkScheduleProvider.WorkScheduleInfo;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class WorkScheduleServiceLogic implements WorkScheduleService {

  @Autowired
  private WorkScheduleRepository workScheduleRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private WorkScheduleMapper mapper;

  @Autowired
  private WorkScheduleProvider workScheduleProvider;

  @Autowired
  private WorkScheduleProperties workScheduleProperties;

  @Autowired
  private WorkScheduleCategoryRepository workScheduleCategoryRepository;

  @Override
  public WorkScheduleData create(WorkScheduleRequests.CreateRequest request) {
    val workSchedule = new WorkSchedule();
    val response = workSchedule.apply(mapper.map(request));
    if (workScheduleRepository.exists(workSchedule.getId())) {
      throw new WorkScheduleExceptions.AlreadyExistsException();
    }
    if (workScheduleRepository.exists(workSchedule.getCategory().getId(), workSchedule.getDate())) {
      throw new WorkScheduleExceptions.AlreadyExistsException();
    }
    val created = workScheduleRepository.create(workSchedule);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WorkScheduleRequests.DeleteRequest request) {
    val workSchedule = workScheduleRepository.findBy(request.getId())
      .orElseThrow(WorkScheduleExceptions.NotFoundException::new);
    val response = workSchedule.apply(mapper.map(request));
    workScheduleRepository.deleteBy(workSchedule.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(WorkScheduleId id) {
    return workScheduleRepository.exists(id);
  }

  public static void main(String... args) {
    System.out.println(OffsetDateTime.now());
  }

  @Override
  public WorkScheduleData get(WorkScheduleId id) {
    return workScheduleRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WorkScheduleExceptions.NotFoundException::new);
  }

  @Override
  public WorkScheduleData get(WorkScheduleCategoryId categoryId, LocalDate date) {
    return workScheduleRepository.findBy(categoryId, date)
      .map(mapper::map)
      .orElseThrow(WorkScheduleExceptions.NotFoundException::new);
  }

  @Override
  public WorkScheduleCategory get(WorkScheduleCategoryId id) {
    return workScheduleCategoryRepository.findBy(id)
      .orElseThrow(WorkScheduleExceptions.CategoryNotFoundException::new);
  }

  @Override
  public void update(WorkScheduleRequests.UpdateRequest request) {
    val workSchedule = workScheduleRepository.findBy(request.getId())
      .orElseThrow(WorkScheduleExceptions.NotFoundException::new);
    val response = workSchedule.apply(mapper.map(request));
    workScheduleRepository.update(workSchedule);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void generate(WorkScheduleRequests.GenerateRequest request) {
    workScheduleCategoryRepository.findAll()
      .forEach(category -> {
        val exists = workScheduleRepository
          .findAllBetween(category.getId(), request.getBegin(), request.getEnd())
          .collect(Collectors.toMap(day -> day.getDate(), day -> true));
        val provided = workScheduleProvider
          .findAllBetween(category, request.getBegin(), request.getEnd())
          .collect(Collectors.toMap(day -> day.getDate(), day -> day));
        Stream.iterate(request.getBegin(), date -> date.plusDays(1))
          .limit(ChronoUnit.DAYS.between(request.getBegin(), request.getEnd()) + 1)
          .filter(date -> !exists.containsKey(date))
          .forEach(date -> {
            val holiday = workScheduleProperties.isHoliday(date.getDayOfWeek());
            val info = provided.containsKey(date) ?
              provided.get(date) :
              WorkScheduleInfo.builder()
                .date(date)
                .category(category)
                .times(
                  holiday ? Collections.emptyList() :
                    category.getTimes().stream().map(mapper::map).collect(Collectors.toList()))
                .holiday(holiday)
                .build();
            val workSchedule = new WorkSchedule();
            val response = workSchedule.apply(mapper.map(info));
            val created = workScheduleRepository.create(workSchedule);
            eventPublisher.publishEvents(response.getEvents());
          });
        eventPublisher.publishEvent(
          new WorkScheduleEvents.GeneratedEvent(request.getBegin(), request.getEnd())
        );
      });

  }

  @Override
  public OffsetDateTime calculateEnd(CalculateEndRequest request) {
    val category = get(request.getCategoryId());
    val begin = request.getBegin();
    val workSchedule = workScheduleRepository
      .findBy(request.getCategoryId(), request.getBegin().toLocalDate())
      .orElseThrow(WorkScheduleExceptions.NotFoundException::new);
    if (!workSchedule.isScheduled(begin)) {
      throw new WorkScheduleExceptions.IllegalTimeException();
    }
    val schedules = workScheduleRepository
      .findAllAfter(request.getCategoryId(), request.getBegin().toLocalDate())
      .sorted(Comparator.comparing(WorkSchedule::getDate))
      .collect(Collectors.toList());

    long remainedMinutes = request.getDurationMinutes();
    OffsetDateTime calculated = null;

    for (val schedule : schedules) {
      for (val time : schedule.getTimes()) {
        val scheduledBegin = schedule.atOffset(time.getBegin());
        val scheduledEnd = schedule.atOffset(time.getEnd());
        if (scheduledEnd.isBefore(begin)) {
          continue;
        }
        long minutes = ChronoUnit.MINUTES.between(scheduledBegin, scheduledEnd);
        if (scheduledBegin.isBefore(begin)) {
          minutes -= ChronoUnit.MINUTES.between(scheduledBegin, begin);
        }
        if (remainedMinutes > minutes) {
          remainedMinutes -= minutes;
        } else {
          calculated = scheduledBegin.plusMinutes(remainedMinutes);
          break;
        }
      }
      if (calculated != null) {
        break;
      }
    }
    return calculated;
  }

}
