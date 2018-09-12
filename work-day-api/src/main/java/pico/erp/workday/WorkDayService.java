package pico.erp.workday;

import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.workday.category.data.WorkDayCategory;
import pico.erp.workday.category.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkDayData;
import pico.erp.workday.data.WorkDayId;

public interface WorkDayService {

  WorkDayData create(@Valid WorkDayRequests.CreateRequest request);

  void delete(@Valid WorkDayRequests.DeleteRequest request);

  boolean exists(@NotNull WorkDayId id);

  void generate(@Valid WorkDayRequests.GenerateRequest request);

  WorkDayData get(@NotNull WorkDayId id);

  WorkDayData get(@NotNull WorkDayCategoryId categoryId, LocalDate date);

  void update(@Valid WorkDayRequests.UpdateRequest request);

  WorkDayCategory get(@NotNull WorkDayCategoryId id);


}
