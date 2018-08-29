package pico.erp.workday.impl;

import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.workday.WorkDayExceptions;
import pico.erp.workday.core.WorkDayCategoryRepository;
import pico.erp.workday.data.WorkDayCategory;
import pico.erp.workday.data.WorkDayCategoryId;
import pico.erp.workday.domain.WorkDay;
import pico.erp.workday.domain.WorkTime;
import pico.erp.workday.impl.jpa.WorkDayEntity;
import pico.erp.workday.impl.jpa.WorkTimeEmbeddable;

@Mapper
public abstract class WorkDayJpaMapper {

  @Autowired
  private WorkDayCategoryRepository workDayCategoryRepository;

  protected WorkDayCategory map(WorkDayCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> workDayCategoryRepository.findBy(categoryId)
        .orElseThrow(WorkDayExceptions.CategoryNotFoundException::new)
      )
      .orElse(null);
  }

  protected WorkTime map(WorkTimeEmbeddable data) {
    return WorkTime.builder()
      .begin(data.getBegin())
      .end(data.getEnd())
      .build();
  }


  protected WorkDay map(WorkDayEntity entity) {
    return WorkDay.builder()
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
  public abstract WorkDayEntity map(WorkDay workDay);

  public abstract WorkTimeEmbeddable map(WorkTime workTime);

  public abstract void pass(WorkDayEntity from, @MappingTarget WorkDayEntity to);

}
