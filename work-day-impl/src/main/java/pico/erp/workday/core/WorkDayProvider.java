package pico.erp.workday.core;

import java.time.LocalDate;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.workday.data.WorkDayCategory;
import pico.erp.workday.domain.WorkDay;

public interface WorkDayProvider {

  Stream<WorkDay> findAllBetween(@NotNull WorkDayCategory category, @NotNull LocalDate begin,
    @NotNull LocalDate end);
}
