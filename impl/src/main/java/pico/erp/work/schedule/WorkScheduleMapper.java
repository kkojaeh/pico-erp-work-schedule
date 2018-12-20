package pico.erp.work.schedule;

import java.util.stream.Collectors;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.work.schedule.category.WorkScheduleCategory;
import pico.erp.work.schedule.category.WorkScheduleCategoryId;
import pico.erp.work.schedule.category.WorkScheduleCategoryMapper;
import pico.erp.work.schedule.provider.WorkScheduleProvider.WorkScheduleInfo;
import pico.erp.work.schedule.time.WorkScheduleTime;
import pico.erp.work.schedule.time.WorkScheduleTimeData;
import pico.erp.work.schedule.time.WorkScheduleTimeMapper;

@org.mapstruct.Mapper(imports = WorkScheduleId.class)
public abstract class WorkScheduleMapper {

  @Lazy
  @Autowired
  protected WorkScheduleCategoryMapper categoryMapper;

  @Lazy
  @Autowired
  protected WorkScheduleTimeMapper timeMapper;


  @Mappings({
    @Mapping(target = "categoryId", source = "category.id")
  })
  public abstract WorkScheduleData map(WorkSchedule workSchedule);

  @Mappings({
    @Mapping(target = "category", source = "categoryId")
  })
  public abstract WorkScheduleMessages.CreateRequest map(
    WorkScheduleRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "id", expression = "java(WorkScheduleId.generate())")
  })
  public abstract WorkScheduleMessages.CreateRequest map(
    WorkScheduleInfo info);

  public abstract WorkScheduleMessages.UpdateRequest map(
    WorkScheduleRequests.UpdateRequest request);

  public abstract WorkScheduleMessages.DeleteRequest map(
    WorkScheduleRequests.DeleteRequest request);

  protected WorkSchedule domain(WorkScheduleEntity entity) {
    return WorkSchedule.builder()
      .id(entity.getId())
      .date(entity.getDate())
      .category(map(entity.getCategoryId()))
      .name(entity.getName())
      .times(entity.getTimes().stream().map(timeMapper::domain).collect(Collectors.toList()))
      .holiday(entity.isHoliday())
      .build();
  }

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract WorkScheduleEntity entity(WorkSchedule workSchedule);

  protected WorkScheduleCategory map(WorkScheduleCategoryId categoryId) {
    return categoryMapper.map(categoryId);
  }

  protected WorkScheduleTime map(WorkScheduleTimeData time) {
    return timeMapper.map(time);
  }

  public abstract void pass(WorkScheduleEntity from, @MappingTarget WorkScheduleEntity to);

}
