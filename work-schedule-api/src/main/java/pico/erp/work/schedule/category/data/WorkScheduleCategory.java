package pico.erp.work.schedule.category.data;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.work.schedule.data.WorkScheduleTimeData;

public interface WorkScheduleCategory {

  WorkScheduleCategoryId getId();

  String getName();

  List<WorkScheduleTimeData> getTimes();

  @Getter
  @AllArgsConstructor
  class WorkScheduleCategoryImpl implements WorkScheduleCategory {

    WorkScheduleCategoryId id;

    String name;

    List<WorkScheduleTimeData> times;

  }

}
