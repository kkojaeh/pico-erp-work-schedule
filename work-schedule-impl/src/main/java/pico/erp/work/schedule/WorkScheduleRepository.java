package pico.erp.work.schedule;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.work.schedule.category.data.WorkScheduleCategoryId;
import pico.erp.work.schedule.data.WorkScheduleId;

@Repository
public interface WorkScheduleRepository {

  WorkSchedule create(@NotNull WorkSchedule workSchedule);

  void deleteBy(@NotNull WorkScheduleId id);

  boolean exists(@NotNull WorkScheduleId id);

  boolean exists(@NotNull WorkScheduleCategoryId categoryId, @NotNull LocalDate date);

  Stream<WorkSchedule> findAllBetween(
    @NotNull WorkScheduleCategoryId categoryId, @NotNull LocalDate begin, @NotNull LocalDate end);

  Stream<WorkSchedule> findAllAfter(
    @NotNull WorkScheduleCategoryId categoryId, @NotNull LocalDate other);

  Optional<WorkSchedule> findBy(@NotNull WorkScheduleId id);

  Optional<WorkSchedule> findBy(@NotNull WorkScheduleCategoryId categoryId,
    @NotNull LocalDate date);

  void update(@NotNull WorkSchedule workSchedule);


}
