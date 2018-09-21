package pico.erp.work.schedule.jpa;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.work.schedule.WorkSchedule;
import pico.erp.work.schedule.WorkScheduleRepository;
import pico.erp.work.schedule.category.data.WorkScheduleCategoryId;
import pico.erp.work.schedule.data.WorkScheduleId;

@Repository
interface WorkScheduleEntityRepository extends CrudRepository<WorkScheduleEntity, WorkScheduleId> {

  @Query("SELECT CASE WHEN COUNT(ws) > 0 THEN true ELSE false END FROM WorkSchedule ws WHERE ws.categoryId = :categoryId AND ws.date = :date")
  boolean exists(
    @Param("categoryId") WorkScheduleCategoryId categoryId,
    @Param("date") LocalDate date
  );

  @Query("SELECT ws FROM WorkSchedule ws WHERE ws.categoryId = :categoryId AND ws.date >= :other ORDER BY ws.date ASC")
  Stream<WorkScheduleEntity> findAllAfter(
    @Param("categoryId") WorkScheduleCategoryId categoryId,
    @Param("other") LocalDate other);

  @Query("SELECT ws FROM WorkSchedule ws WHERE ws.categoryId = :categoryId AND ws.date >= :begin AND ws.date <= :end ORDER BY ws.date ASC")
  Stream<WorkScheduleEntity> findAllBetween(
    @Param("categoryId") WorkScheduleCategoryId categoryId,
    @Param("begin") LocalDate begin,
    @Param("end") LocalDate end);

  @Query("SELECT ws FROM WorkSchedule ws WHERE ws.categoryId = :categoryId AND ws.date = :date")
  Optional<WorkScheduleEntity> findBy(
    @Param("categoryId") WorkScheduleCategoryId categoryId,
    @Param("date") LocalDate date
  );

}

@Repository
@Transactional
public class WorkScheduleRepositoryJpa implements WorkScheduleRepository {

  @Autowired
  private WorkScheduleEntityRepository repository;

  @Autowired
  private JpaMapper mapper;


  @Override
  public WorkSchedule create(WorkSchedule workSchedule) {
    val entity = mapper.map(workSchedule);
    val created = repository.save(entity);
    return mapper.map(created);
  }

  @Override
  public void deleteBy(WorkScheduleId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(WorkScheduleId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(WorkScheduleCategoryId categoryId, LocalDate date) {
    return repository.exists(categoryId, date);
  }

  @Override
  public Stream<WorkSchedule> findAllBetween(WorkScheduleCategoryId categoryId, LocalDate begin,
    LocalDate end) {
    return repository.findAllBetween(categoryId, begin, end)
      .map(mapper::map);
  }

  @Override
  public Stream<WorkSchedule> findAllAfter(@NotNull WorkScheduleCategoryId categoryId,
    @NotNull LocalDate base) {
    return repository.findAllAfter(categoryId, base)
      .map(mapper::map);
  }

  @Override
  public Optional<WorkSchedule> findBy(WorkScheduleId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public Optional<WorkSchedule> findBy(WorkScheduleCategoryId categoryId, LocalDate date) {
    return repository.findBy(categoryId, date)
      .map(mapper::map);
  }

  @Override
  public void update(WorkSchedule workSchedule) {
    val entity = repository.findOne(workSchedule.getId());
    mapper.pass(mapper.map(workSchedule), entity);
    repository.save(entity);
  }
}
