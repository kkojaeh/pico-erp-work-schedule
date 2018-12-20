package pico.erp.work.schedule;

import java.time.DayOfWeek;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Data
@Configuration
@ConfigurationProperties("work-schedule")
public class WorkScheduleProperties {

  WorkScheduleICalendarProvidePolicy iCalendarProvidePolicy;

  List<DayOfWeek> holidays;

  public boolean isHoliday(DayOfWeek day) {
    if (holidays != null) {
      return holidays.contains(day);
    }
    return false;
  }


  @Data
  public static class WorkScheduleICalendarProvidePolicy {

    Resource location;

    boolean gzipped;

    String[] holidayKeywords;

    boolean holidayOnly;

  }

}
