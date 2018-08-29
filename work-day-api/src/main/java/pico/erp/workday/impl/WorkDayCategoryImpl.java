package pico.erp.workday.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.workday.data.WorkDayCategory;
import pico.erp.workday.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkTimeData;

@Getter
@AllArgsConstructor
public class WorkDayCategoryImpl implements WorkDayCategory {

  WorkDayCategoryId id;

  String name;

  List<WorkTimeData> times;

}
