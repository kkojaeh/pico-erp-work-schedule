package pico.erp.workday.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import pico.erp.workday.category.data.WorkDayCategory;
import pico.erp.workday.category.data.WorkDayCategoryId;

@Repository
public class WorkDayCategoryRepositoryImpl implements WorkDayCategoryRepository {

  @Lazy
  @Autowired
  List<WorkDayCategory> workDayCategories;

  @Override
  public Stream<WorkDayCategory> findAll() {
    return workDayCategories.stream();
  }

  @Override
  public Optional<WorkDayCategory> findBy(WorkDayCategoryId id) {
    return workDayCategories.stream()
      .filter(workDayCategory -> workDayCategory.getId().equals(id))
      .findFirst();
  }
}
