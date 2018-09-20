package pico.erp.work.schedule;

import java.time.LocalDate;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.work.schedule.category.data.WorkScheduleCategory;

public interface WorkScheduleProvider {

  Stream<WorkSchedule> findAllBetween(@NotNull WorkScheduleCategory category,
    @NotNull LocalDate begin,
    @NotNull LocalDate end);
}
