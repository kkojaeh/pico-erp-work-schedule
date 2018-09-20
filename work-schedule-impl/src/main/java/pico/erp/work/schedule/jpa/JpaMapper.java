package pico.erp.work.schedule.jpa;

import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.work.schedule.WorkSchedule;
import pico.erp.work.schedule.WorkScheduleExceptions;
import pico.erp.work.schedule.WorkScheduleTime;
import pico.erp.work.schedule.category.WorkScheduleCategoryRepository;
import pico.erp.work.schedule.category.data.WorkScheduleCategory;
import pico.erp.work.schedule.category.data.WorkScheduleCategoryId;

@Mapper
public abstract class JpaMapper {

  @Autowired
  private WorkScheduleCategoryRepository workScheduleCategoryRepository;

  protected WorkScheduleCategory map(WorkScheduleCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> workScheduleCategoryRepository.findBy(categoryId)
        .orElseThrow(WorkScheduleExceptions.CategoryNotFoundException::new)
      )
      .orElse(null);
  }

  protected WorkScheduleTime map(WorkScheduleTimeEmbeddable data) {
    return WorkScheduleTime.builder()
      .begin(data.getBegin())
      .end(data.getEnd())
      .build();
  }


  protected WorkSchedule map(WorkScheduleEntity entity) {
    return WorkSchedule.builder()
      .id(entity.getId())
      .date(entity.getDate())
      .category(map(entity.getCategoryId()))
      .name(entity.getName())
      .times(entity.getTimes().stream().map(this::map).collect(Collectors.toList()))
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
  public abstract WorkScheduleEntity map(WorkSchedule workSchedule);

  public abstract WorkScheduleTimeEmbeddable map(WorkScheduleTime workScheduleTime);

  public abstract void pass(WorkScheduleEntity from, @MappingTarget WorkScheduleEntity to);

}
