package pico.erp.work.schedule.category;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.work.schedule.WorkScheduleExceptions;

@Mapper
public class WorkScheduleCategoryMapper {

  @Autowired
  private WorkScheduleCategoryRepository workScheduleCategoryRepository;

  public WorkScheduleCategory map(WorkScheduleCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> workScheduleCategoryRepository.findBy(categoryId)
        .orElseThrow(WorkScheduleExceptions.CategoryNotFoundException::new)
      )
      .orElse(null);
  }

}
