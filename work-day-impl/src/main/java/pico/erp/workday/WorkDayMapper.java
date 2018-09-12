package pico.erp.workday;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.workday.category.WorkDayCategoryRepository;
import pico.erp.workday.category.data.WorkDayCategory;
import pico.erp.workday.category.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkDayData;
import pico.erp.workday.data.WorkTimeData;

@Mapper
public abstract class WorkDayMapper {

  @Autowired
  private WorkDayCategoryRepository workDayCategoryRepository;

  protected WorkDayCategory map(WorkDayCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> workDayCategoryRepository.findBy(categoryId)
        .orElseThrow(WorkDayExceptions.CategoryNotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id")
  })
  public abstract WorkDayData map(WorkDay workDay);

  public abstract WorkTimeData map(WorkTime workTime);

  public WorkTime map(WorkTimeData data) {
    return WorkTime.builder()
      .begin(data.getBegin())
      .end(data.getEnd())
      .build();
  }

  @Mappings({
    @Mapping(target = "category", source = "categoryId")
  })
  public abstract WorkDayMessages.CreateRequest map(WorkDayRequests.CreateRequest request);

  public abstract WorkDayMessages.UpdateRequest map(WorkDayRequests.UpdateRequest request);

  public abstract WorkDayMessages.DeleteRequest map(WorkDayRequests.DeleteRequest request);

}
