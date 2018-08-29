package pico.erp.workday.data;

import java.util.List;

public interface WorkDayCategory {

  WorkDayCategoryId getId();

  String getName();

  List<WorkTimeData> getTimes();

}
