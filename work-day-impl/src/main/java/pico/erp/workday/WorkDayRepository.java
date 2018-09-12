package pico.erp.workday;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.workday.category.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkDayId;

@Repository
public interface WorkDayRepository {

  WorkDay create(@NotNull WorkDay workDay);

  void deleteBy(@NotNull WorkDayId id);

  boolean exists(@NotNull WorkDayId id);

  boolean exists(@NotNull WorkDayCategoryId categoryId, @NotNull LocalDate date);

  Stream<WorkDay> findAllBetween(
    @NotNull WorkDayCategoryId categoryId, @NotNull LocalDate begin, @NotNull LocalDate end);

  Optional<WorkDay> findBy(@NotNull WorkDayId id);

  Optional<WorkDay> findBy(@NotNull WorkDayCategoryId categoryId, @NotNull LocalDate date);

  void update(@NotNull WorkDay workDay);

}
