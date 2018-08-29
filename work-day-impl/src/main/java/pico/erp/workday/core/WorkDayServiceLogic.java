package pico.erp.workday.core;

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
import pico.erp.workday.WorkDayEvents;
import pico.erp.workday.WorkDayExceptions;
import pico.erp.workday.WorkDayProperties;
import pico.erp.workday.WorkDayRequests;
import pico.erp.workday.WorkDayService;
import pico.erp.workday.data.WorkDayCategory;
import pico.erp.workday.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkDayData;
import pico.erp.workday.data.WorkDayId;
import pico.erp.workday.domain.WorkDay;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WorkDayServiceLogic implements WorkDayService {

  @Autowired
  private WorkDayRepository workDayRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private WorkDayMapper mapper;

  @Lazy
  @Autowired
  private AuditService auditService;

  @Autowired
  private WorkDayProvider workDayProvider;

  @Autowired
  private WorkDayProperties workDayProperties;

  @Autowired
  private WorkDayCategoryRepository workDayCategoryRepository;

  @Override
  public WorkDayData create(WorkDayRequests.CreateRequest request) {
    val workDay = new WorkDay();
    val response = workDay.apply(mapper.map(request));
    if (workDayRepository.exists(workDay.getId())) {
      throw new WorkDayExceptions.AlreadyExistsException();
    }
    if (workDayRepository.exists(workDay.getCategory().getId(), workDay.getDate())) {
      throw new WorkDayExceptions.AlreadyExistsException();
    }
    val created = workDayRepository.create(workDay);
    auditService.commit(created);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WorkDayRequests.DeleteRequest request) {
    val workDay = workDayRepository.findBy(request.getId())
      .orElseThrow(WorkDayExceptions.NotFoundException::new);
    val response = workDay.apply(mapper.map(request));
    workDayRepository.deleteBy(workDay.getId());
    auditService.delete(workDay);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(WorkDayId id) {
    return workDayRepository.exists(id);
  }

  @Override
  public void generate(WorkDayRequests.GenerateRequest request) {
    workDayCategoryRepository.findAll()
      .forEach(category -> {
        val exists = workDayRepository
          .findAllBetween(category.getId(), request.getBegin(), request.getEnd())
          .collect(Collectors.toMap(day -> day.getDate(), day -> true));
        val provided = workDayProvider
          .findAllBetween(category, request.getBegin(), request.getEnd())
          .collect(Collectors.toMap(day -> day.getDate(), day -> day));
        Stream.iterate(request.getBegin(), date -> date.plusDays(1))
          .limit(ChronoUnit.DAYS.between(request.getBegin(), request.getEnd()) + 1)
          .filter(date -> !exists.containsKey(date))
          .forEach(date -> {
            val holiday = workDayProperties.isHoliday(date.getDayOfWeek());
            val workDay = provided.containsKey(date) ?
              provided.get(date) :
              WorkDay.builder()
                .id(WorkDayId.generate())
                .date(date)
                .category(category)
                .times(
                  holiday ? Collections.emptyList() :
                    category.getTimes().stream().map(mapper::map).collect(Collectors.toList()))
                .holiday(holiday)
                .build();
            val created = workDayRepository.create(workDay);
            auditService.commit(created);
          });
        eventPublisher.publishEvent(
          new WorkDayEvents.GeneratedEvent(request.getBegin(), request.getEnd())
        );
      });

  }

  @Override
  public WorkDayData get(WorkDayId id) {
    return workDayRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WorkDayExceptions.NotFoundException::new);
  }

  @Override
  public WorkDayData get(WorkDayCategoryId categoryId, LocalDate date) {
    return workDayRepository.findBy(categoryId, date)
      .map(mapper::map)
      .orElseThrow(WorkDayExceptions.NotFoundException::new);
  }


  @Override
  public void update(WorkDayRequests.UpdateRequest request) {
    val workDay = workDayRepository.findBy(request.getId())
      .orElseThrow(WorkDayExceptions.NotFoundException::new);
    val response = workDay.apply(mapper.map(request));
    workDayRepository.update(workDay);
    auditService.commit(workDay);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public WorkDayCategory get(WorkDayCategoryId id) {
    return workDayCategoryRepository.findBy(id)
      .orElseThrow(WorkDayExceptions.CategoryNotFoundException::new);
  }

}
