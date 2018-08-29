package pico.erp.workday.core;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.workday.data.WorkDayCategory;
import pico.erp.workday.data.WorkDayCategoryId;

public interface WorkDayCategoryRepository {

  Stream<WorkDayCategory> findAll();

  Optional<WorkDayCategory> findBy(@NotNull WorkDayCategoryId id);

}
