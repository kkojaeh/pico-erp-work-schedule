package pico.erp.workday.jpa;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.ExtendedLabeledValue;
import pico.erp.shared.Public;
import pico.erp.shared.data.LabeledValuable;
import pico.erp.shared.jpa.QueryDslJpaSupport;
import pico.erp.workday.WorkDayQuery;
import pico.erp.workday.category.WorkDayCategoryRepository;
import pico.erp.workday.data.WorkDayView;
import pico.erp.workday.data.WorkTimeData;

@Service
@Public
@Transactional(readOnly = true)
@Validated
public class WorkDayQueryJpa implements WorkDayQuery {

  private final QWorkDayEntity workDay = QWorkDayEntity.workDayEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Autowired
  private WorkDayCategoryRepository workDayCategoryRepository;


  @Override
  public List<? extends LabeledValuable> asCategoryLabels() {
    return workDayCategoryRepository.findAll()
      .map(category -> ExtendedLabeledValue.builder()
        .value(category.getId().getValue())
        .label(category.getName())
        .build()
      ).collect(Collectors.toList());
  }

  protected WorkTimeData map(WorkTimeEmbeddable data) {
    return new WorkTimeData(data.getBegin(), data.getEnd());
  }

  @Override
  public List<WorkDayView> retrieve(WorkDayView.Filter filter) {
    if (ChronoUnit.DAYS.between(filter.getBegin(), filter.getEnd()) > 60) {
      throw new IllegalArgumentException("period too long");
    }

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getBegin())) {
      builder.and(workDay.date.goe(filter.getBegin()));
    }

    if (!isEmpty(filter.getEnd())) {
      builder.and(workDay.date.loe(filter.getEnd()));
    }

    val query = new JPAQuery<WorkDayEntity>(entityManager);

    query.select(workDay);
    query.from(workDay);
    query.where(builder);
    query.orderBy(workDay.date.asc());
    return query.fetch().stream()
      .map(entity -> WorkDayView.builder()
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
