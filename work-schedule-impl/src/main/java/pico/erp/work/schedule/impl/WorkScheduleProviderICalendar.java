package pico.erp.work.schedule.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import lombok.SneakyThrows;
import lombok.val;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.RRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pico.erp.work.schedule.Mapper;
import pico.erp.work.schedule.WorkSchedule;
import pico.erp.work.schedule.WorkScheduleProperties;
import pico.erp.work.schedule.WorkScheduleProvider;
import pico.erp.work.schedule.category.data.WorkScheduleCategory;
import pico.erp.work.schedule.data.WorkScheduleId;

@Component
public class WorkScheduleProviderICalendar implements WorkScheduleProvider {

  @Autowired
  WorkScheduleProperties workScheduleProperties;

  @Autowired
  private Mapper mapper;

  @SneakyThrows
  @Override
  public Stream<WorkSchedule> findAllBetween(WorkScheduleCategory category, LocalDate begin,
    LocalDate end) {
    val policy = workScheduleProperties.getICalendarProvidePolicy();
    val location = policy.getLocation();
    val builder = new CalendarBuilder();
    val calendar = policy.isGzipped() ?
      builder.build(new GZIPInputStream(location.getInputStream())) :
      builder.build(location.getInputStream());

    return calendar.getComponents().stream()
      .filter(component -> component instanceof VEvent)
      .map(component -> (VEvent) component)
      .flatMap(event -> this.map(category, event, begin, end));
  }

  private Stream<WorkSchedule> map(WorkScheduleCategory category, VEvent event, LocalDate begin,
    LocalDate end) {
    val zoneId = ZoneId.systemDefault();
    val endDate = Date.from(end.atStartOfDay().atZone(zoneId).toInstant());

    val policy = workScheduleProperties.getICalendarProvidePolicy();
    val summary = event.getSummary().getValue();
    val keyword = event.getProperty(Property.CATEGORIES).getValue();
    boolean includedKeyword = false;
    for (String holidayKeyword : policy.getHolidayKeywords()) {
      if (keyword.indexOf(holidayKeyword) > -1) {
        includedKeyword = true;
      }
    }
    val holidayEvent = includedKeyword;
    if (policy.isHolidayOnly() && !holidayEvent) {
      return Stream.empty();
    }
    RRule rrule = event.getProperty(Property.RRULE);
    val dates = rrule != null ? rrule.getRecur().getDates(
      event.getStartDate().getDate(),
      new net.fortuna.ical4j.model.Date(endDate),
      Value.DATE) :
      Arrays.asList(event.getStartDate().getDate());

    return dates.stream()
      .map(date -> date.toInstant().atZone(zoneId).toLocalDate())
      .filter(date -> !date.isBefore(begin) && !date.isAfter(end))
      .map(date -> {
        val holiday = holidayEvent || workScheduleProperties.isHoliday(date.getDayOfWeek());
        return WorkSchedule.builder()
          .id(WorkScheduleId.generate())
          .date(date)
          .category(category)
          .times(
            holiday ? Collections.emptyList()
              : category.getTimes().stream().map(mapper::map).collect(Collectors.toList())
          )
          .name(summary)
          .holiday(holiday)
          .build();
      });
  }
}
