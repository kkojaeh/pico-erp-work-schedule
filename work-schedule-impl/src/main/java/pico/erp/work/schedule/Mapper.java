package pico.erp.work.schedule;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.work.schedule.category.WorkScheduleCategoryRepository;
import pico.erp.work.schedule.category.data.WorkScheduleCategory;
import pico.erp.work.schedule.category.data.WorkScheduleCategoryId;
import pico.erp.work.schedule.data.WorkScheduleData;
import pico.erp.work.schedule.data.WorkScheduleTimeData;

@org.mapstruct.Mapper
public abstract class Mapper {

  @Autowired
  private WorkScheduleCategoryRepository workScheduleCategoryRepository;

  protected WorkScheduleCategory map(WorkScheduleCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> workScheduleCategoryRepository.findBy(categoryId)
        .orElseThrow(WorkScheduleExceptions.CategoryNotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id")
  })
  public abstract WorkScheduleData map(WorkSchedule workSchedule);

  public abstract WorkScheduleTimeData map(WorkScheduleTime workScheduleTime);

  public WorkScheduleTime map(WorkScheduleTimeData data) {
    return WorkScheduleTime.builder()
      .begin(data.getBegin())
      .end(data.getEnd())
      .build();
  }

  @Mappings({
    @Mapping(target = "category", source = "categoryId")
  })
  public abstract WorkScheduleMessages.CreateRequest map(
    WorkScheduleRequests.CreateRequest request);

  public abstract WorkScheduleMessages.UpdateRequest map(
    WorkScheduleRequests.UpdateRequest request);

  public abstract WorkScheduleMessages.DeleteRequest map(
    WorkScheduleRequests.DeleteRequest request);

}
