package pico.erp.work.schedule.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
public class WorkScheduleCategoryRepositoryImpl implements WorkScheduleCategoryRepository {

  @Lazy
  @Autowired
  List<WorkScheduleCategory> workScheduleCategories;

  @Override
  public Stream<WorkScheduleCategory> findAll() {
    return workScheduleCategories.stream();
  }

  @Override
  public Optional<WorkScheduleCategory> findBy(WorkScheduleCategoryId id) {
    return workScheduleCategories.stream()
      .filter(category -> category.getId().equals(id))
      .findFirst();
  }
}
