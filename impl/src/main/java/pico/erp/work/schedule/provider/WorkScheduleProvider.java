package pico.erp.work.schedule.provider;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import pico.erp.work.schedule.category.WorkScheduleCategory;
import pico.erp.work.schedule.time.WorkScheduleTime;

public interface WorkScheduleProvider {

  Stream<WorkScheduleInfo> findAllBetween(@NotNull WorkScheduleCategory category,
    @NotNull LocalDate begin,
    @NotNull LocalDate end);

  @Getter
  @Builder
  class WorkScheduleInfo {

    WorkScheduleCategory category;

    LocalDate date;

    String name;

    boolean holiday;

    List<WorkScheduleTime> times;

  }
}
