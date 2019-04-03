package pico.erp.work.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.work.schedule.category.WorkScheduleCategory;
import pico.erp.work.schedule.category.WorkScheduleCategoryId;

public interface WorkScheduleService {

  WorkScheduleData create(@Valid WorkScheduleRequests.CreateRequest request);

  void delete(@Valid WorkScheduleRequests.DeleteRequest request);

  boolean exists(@NotNull WorkScheduleId id);

  void generate(@Valid WorkScheduleRequests.GenerateRequest request);

  WorkScheduleData get(@NotNull WorkScheduleId id);

  WorkScheduleData get(@NotNull WorkScheduleCategoryId categoryId, LocalDate date);

  WorkScheduleCategory get(@NotNull WorkScheduleCategoryId id);

  void update(@Valid WorkScheduleRequests.UpdateRequest request);

  LocalDateTime calculateEnd(@Valid WorkScheduleRequests.CalculateEndRequest request);


}
