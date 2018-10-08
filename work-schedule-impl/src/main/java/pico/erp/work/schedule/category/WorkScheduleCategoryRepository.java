package pico.erp.work.schedule.category;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface WorkScheduleCategoryRepository {

  Stream<WorkScheduleCategory> findAll();

  Optional<WorkScheduleCategory> findBy(@NotNull WorkScheduleCategoryId id);

}
