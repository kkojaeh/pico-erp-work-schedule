package pico.erp.workday.impl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.workday.core.WorkDayRepository;
import pico.erp.workday.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkDayId;
import pico.erp.workday.domain.WorkDay;
import pico.erp.workday.impl.jpa.WorkDayEntity;

@Repository
interface WorkDayEntityRepository extends CrudRepository<WorkDayEntity, WorkDayId> {

  @Query("SELECT CASE WHEN COUNT(wd) > 0 THEN true ELSE false END FROM WorkDay wd WHERE wd.categoryId = :categoryId AND wd.date = :date")
  boolean exists(
    @Param("categoryId") WorkDayCategoryId categoryId,
    @Param("date") LocalDate date
  );

  @Query("SELECT wd FROM WorkDay wd WHERE wd.categoryId = :categoryId AND wd.date >= :begin AND wd.date <= :end")
  Stream<WorkDayEntity> findAllBetween(
    @Param("categoryId") WorkDayCategoryId categoryId,
    @Param("begin") LocalDate begin,
    @Param("end") LocalDate end);

  @Query("SELECT wd FROM WorkDay wd WHERE wd.categoryId = :categoryId AND wd.date = :date")
  Optional<WorkDayEntity> findBy(
    @Param("categoryId") WorkDayCategoryId categoryId,
    @Param("date") LocalDate date
  );

}

@Repository
@Transactional
public class WorkDayRepositoryJpa implements WorkDayRepository {

  @Autowired
  private WorkDayEntityRepository repository;

  @Autowired
  private WorkDayJpaMapper mapper;


  @Override
  public WorkDay create(WorkDay workDay) {
    val entity = mapper.map(workDay);
    val created = repository.save(entity);
    return mapper.map(created);
  }

  @Override
  public void deleteBy(WorkDayId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(WorkDayId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(WorkDayCategoryId categoryId, LocalDate date) {
    return repository.exists(categoryId, date);
  }

  @Override
  public Stream<WorkDay> findAllBetween(WorkDayCategoryId categoryId, LocalDate begin,
    LocalDate end) {
    return repository.findAllBetween(categoryId, begin, end)
      .map(mapper::map);
  }

  @Override
  public Optional<WorkDay> findBy(WorkDayId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public Optional<WorkDay> findBy(WorkDayCategoryId categoryId, LocalDate date) {
    return repository.findBy(categoryId, date)
      .map(mapper::map);
  }

  @Override
  public void update(WorkDay workDay) {
    val entity = repository.findOne(workDay.getId());
    mapper.pass(mapper.map(workDay), entity);
    repository.save(entity);
  }
}
