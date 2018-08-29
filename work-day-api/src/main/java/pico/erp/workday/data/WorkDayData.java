package pico.erp.workday.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WorkDayData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WorkDayId id;

  LocalDate date;

  WorkDayCategoryId categoryId;

  String name;

  List<WorkTimeData> times;

  boolean holiday;

}
