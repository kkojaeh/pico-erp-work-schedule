package pico.erp.workday.category;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.workday.category.data.WorkDayCategory;
import pico.erp.workday.category.data.WorkDayCategoryId;

public interface WorkDayCategoryRepository {

  Stream<WorkDayCategory> findAll();

  Optional<WorkDayCategory> findBy(@NotNull WorkDayCategoryId id);

}
