package pico.erp.work.schedule;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.ExtendedLabeledValue;
import pico.erp.shared.data.LabeledValuable;
import pico.erp.shared.jpa.QueryDslJpaSupport;
import pico.erp.work.schedule.category.WorkScheduleCategoryRepository;
import pico.erp.work.schedule.time.WorkScheduleTimeData;
import pico.erp.work.schedule.time.WorkScheduleTimeEmbeddable;

@Service
@ComponentBean
@Transactional(readOnly = true)
@Validated
public class WorkScheduleQueryJpa implements WorkScheduleQuery {

  private final QWorkScheduleEntity workSchedule = QWorkScheduleEntity.workScheduleEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Autowired
  private WorkScheduleCategoryRepository workScheduleCategoryRepository;


  @Override
  public List<? extends LabeledValuable> asCategoryLabels() {
    return workScheduleCategoryRepository.findAll()
      .map(category -> ExtendedLabeledValue.builder()
        .value(category.getId().getValue())
        .label(category.getName())
        .build()
      ).collect(Collectors.toList());
  }

  protected WorkScheduleTimeData map(WorkScheduleTimeEmbeddable data) {
    return new WorkScheduleTimeData(data.getBegin(), data.getEnd());
  }

  @Override
  public List<WorkScheduleView> retrieve(WorkScheduleView.Filter filter) {
    if (ChronoUnit.DAYS.between(filter.getBegin(), filter.getEnd()) > 60) {
      throw new IllegalArgumentException("period too long");
    }

    val builder = new BooleanBuilder();

    builder.and(workSchedule.categoryId.eq(filter.getCategoryId()));

    if (!isEmpty(filter.getBegin())) {
      builder.and(workSchedule.date.goe(filter.getBegin()));
    }

    if (!isEmpty(filter.getEnd())) {
      builder.and(workSchedule.date.loe(filter.getEnd()));
    }

    val query = new JPAQuery<WorkScheduleEntity>(entityManager);

    query.select(workSchedule);
    query.from(workSchedule);
    query.where(builder);
    query.orderBy(workSchedule.date.asc());
    return query.fetch().stream()
      .map(entity -> WorkScheduleView.builder()
        .id(entity.getId())
        .date(entity.getDate())
        .categoryId(entity.getCategoryId())
        .name(entity.getName())
        .holiday(entity.isHoliday())
        .times(entity.getTimes().stream().map(this::map).collect(Collectors.toList()))
        .build()
      ).collect(Collectors.toList());
  }
}
