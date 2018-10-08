package pico.erp.work.schedule;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.work.schedule.category.WorkScheduleCategoryId;
import pico.erp.work.schedule.time.WorkScheduleTimeData;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkScheduleView {

  WorkScheduleId id;

  String name;

  boolean holiday;

  LocalDate date;

  WorkScheduleCategoryId categoryId;

  List<WorkScheduleTimeData> times;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    @NotNull
    WorkScheduleCategoryId categoryId;

    @NotNull
    LocalDate begin;

    @NotNull
    LocalDate end;

  }

}
