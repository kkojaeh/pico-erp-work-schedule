package pico.erp.workday.category.data;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.workday.data.WorkTimeData;

public interface WorkDayCategory {

  WorkDayCategoryId getId();

  String getName();

  List<WorkTimeData> getTimes();

  @Getter
  @AllArgsConstructor
  class WorkDayCategoryImpl implements WorkDayCategory {

    WorkDayCategoryId id;

    String name;

    List<WorkTimeData> times;

  }

}
