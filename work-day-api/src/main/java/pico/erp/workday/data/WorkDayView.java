package pico.erp.workday.data;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.workday.category.data.WorkDayCategoryId;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkDayView {

  WorkDayId id;

  String name;

  boolean holiday;

  LocalDate date;

  WorkDayCategoryId categoryId;

  List<WorkTimeData> times;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    @NotNull
    WorkDayCategoryId categoryId;

    @NotNull
    LocalDate begin;

    @NotNull
    LocalDate end;

  }

}
