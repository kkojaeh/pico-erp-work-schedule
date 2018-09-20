package pico.erp.work.schedule;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.audit.AuditService;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;
import pico.erp.work.schedule.category.WorkScheduleCategoryRepository;
import pico.erp.work.schedule.category.data.WorkScheduleCategory;
import pico.erp.work.schedule.category.data.WorkScheduleCategoryId;
import pico.erp.work.schedule.data.WorkScheduleData;
import pico.erp.work.schedule.data.WorkScheduleId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WorkScheduleServiceLogic implements WorkScheduleService {

  @Autowired
  private WorkScheduleRepository workScheduleRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private Mapper mapper;

  @Lazy
  @Autowired
  private AuditService auditService;

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
    auditService.commit(created);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WorkScheduleRequests.DeleteRequest request) {
    val workSchedule = workScheduleRepository.findBy(request.getId())
      .orElseThrow(WorkScheduleExceptions.NotFoundException::new);
    val response = workSchedule.apply(mapper.map(request));
    workScheduleRepository.deleteBy(workSchedule.getId());
    auditService.delete(workSchedule);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(WorkScheduleId id) {
    return workScheduleRepository.exists(id);
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
            val workSchedule = provided.containsKey(date) ?
              provided.get(date) :
              WorkSchedule.builder()
                .id(WorkScheduleId.generate())
                .date(date)
                .category(category)
                .times(
                  holiday ? Collections.emptyList() :
                    category.getTimes().stream().map(mapper::map).collect(Collectors.toList()))
                .holiday(holiday)
                .build();
            val created = workScheduleRepository.create(workSchedule);
            auditService.commit(created);
          });
        eventPublisher.publishEvent(
          new WorkScheduleEvents.GeneratedEvent(request.getBegin(), request.getEnd())
        );
      });

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
    auditService.commit(workSchedule);
    eventPublisher.publishEvents(response.getEvents());
  }

}
