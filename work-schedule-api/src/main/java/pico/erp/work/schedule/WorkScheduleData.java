package pico.erp.work.schedule;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.work.schedule.category.WorkScheduleCategoryId;
import pico.erp.work.schedule.time.WorkScheduleTimeData;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WorkScheduleData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WorkScheduleId id;

  LocalDate date;

  WorkScheduleCategoryId categoryId;

  String name;

  List<WorkScheduleTimeData> times;

  boolean holiday;

}
