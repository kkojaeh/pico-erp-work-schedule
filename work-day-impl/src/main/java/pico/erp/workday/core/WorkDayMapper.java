package pico.erp.workday.core;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.workday.WorkDayExceptions;
import pico.erp.workday.WorkDayRequests;
import pico.erp.workday.data.WorkDayCategory;
import pico.erp.workday.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkDayData;
import pico.erp.workday.data.WorkTimeData;
import pico.erp.workday.domain.WorkDay;
import pico.erp.workday.domain.WorkDayMessages;
import pico.erp.workday.domain.WorkTime;

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
