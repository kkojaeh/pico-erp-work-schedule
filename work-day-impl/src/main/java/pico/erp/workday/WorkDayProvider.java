package pico.erp.workday;

import java.time.LocalDate;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.workday.category.data.WorkDayCategory;

public interface WorkDayProvider {

  Stream<WorkDay> findAllBetween(@NotNull WorkDayCategory category, @NotNull LocalDate begin,
    @NotNull LocalDate end);
}
