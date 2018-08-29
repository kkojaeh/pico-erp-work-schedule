package pico.erp.workday;

import java.time.DayOfWeek;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Data
@Configuration
@ConfigurationProperties("work-day")
public class WorkDayProperties {

  WorkDayICalendarProvidePolicy iCalendarProvidePolicy;

  List<DayOfWeek> holidays;

  public boolean isHoliday(DayOfWeek day) {
    if (holidays != null) {
      return holidays.contains(day);
    }
    return false;
  }


  @Data
  public static class WorkDayICalendarProvidePolicy {

    Resource location;

    boolean gzipped;

    String[] holidayKeywords;

    boolean holidayOnly;

  }

}
