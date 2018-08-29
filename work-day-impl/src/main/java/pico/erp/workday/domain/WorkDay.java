package pico.erp.workday.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.audit.annotation.Audit;
import pico.erp.workday.WorkDayEvents;
import pico.erp.workday.WorkDayExceptions;
import pico.erp.workday.data.WorkDayCategory;
import pico.erp.workday.data.WorkDayId;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "work-day")
public class WorkDay implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WorkDayId id;

  WorkDayCategory category;

  LocalDate date;

  String name;

  boolean holiday;

  List<WorkTime> times;

  public WorkDay() {
    this.times = new LinkedList<>();
  }

  public WorkDayMessages.CreateResponse apply(WorkDayMessages.CreateRequest request) {
    id = request.getId();
    category = request.getCategory();
    date = request.getDate();
    name = request.getName();
    holiday = request.isHoliday();
    times = request.getTimes();
    return new WorkDayMessages.CreateResponse(
      Arrays.asList(new WorkDayEvents.CreatedEvent(this.id)));
  }

  public WorkDayMessages.UpdateResponse apply(WorkDayMessages.UpdateRequest request) {
    val now = LocalDate.now();
    // 오늘과 같거나 예전 데이터를 수정하면 오류
    if (now.isAfter(date) || now.equals(date)) {
      throw new WorkDayExceptions.CannotModifyException();
    }
    name = request.getName();
    holiday = request.isHoliday();
    times = request.getTimes();
    return new WorkDayMessages.UpdateResponse(
      Arrays.asList(new WorkDayEvents.UpdatedEvent(this.id)));
  }

  public WorkDayMessages.DeleteResponse apply(WorkDayMessages.DeleteRequest request) {
    return new WorkDayMessages.DeleteResponse(
      Arrays.asList(new WorkDayEvents.DeletedEvent(this.id)));
  }


}
