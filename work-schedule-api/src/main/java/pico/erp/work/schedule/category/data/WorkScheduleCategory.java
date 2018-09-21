package pico.erp.work.schedule.category.data;

import java.time.ZoneId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.work.schedule.data.WorkScheduleTimeData;

public interface WorkScheduleCategory {

  WorkScheduleCategoryId getId();

  String getName();

  List<WorkScheduleTimeData> getTimes();

  ZoneId getZoneId();

  @Getter
  @AllArgsConstructor
  class WorkScheduleCategoryImpl implements WorkScheduleCategory {

    WorkScheduleCategoryId id;

    String name;

    ZoneId zoneId;

    List<WorkScheduleTimeData> times;

  }

}
