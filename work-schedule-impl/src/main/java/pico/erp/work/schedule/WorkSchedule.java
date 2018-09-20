package pico.erp.work.schedule;

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
import pico.erp.work.schedule.category.data.WorkScheduleCategory;
import pico.erp.work.schedule.data.WorkScheduleId;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "work-schedule")
public class WorkSchedule implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WorkScheduleId id;

  WorkScheduleCategory category;

  LocalDate date;

  String name;

  boolean holiday;

  List<WorkScheduleTime> times;

  public WorkSchedule() {
    this.times = new LinkedList<>();
  }

  public WorkScheduleMessages.CreateResponse apply(WorkScheduleMessages.CreateRequest request) {
    id = request.getId();
    category = request.getCategory();
    date = request.getDate();
    name = request.getName();
    holiday = request.isHoliday();
    times = request.getTimes();
    return new WorkScheduleMessages.CreateResponse(
      Arrays.asList(new WorkScheduleEvents.CreatedEvent(this.id)));
  }

  public WorkScheduleMessages.UpdateResponse apply(WorkScheduleMessages.UpdateRequest request) {
    val now = LocalDate.now();
    // 오늘과 같거나 예전 데이터를 수정하면 오류
    if (now.isAfter(date) || now.equals(date)) {
      throw new WorkScheduleExceptions.CannotModifyException();
    }
    name = request.getName();
    holiday = request.isHoliday();
    times = request.getTimes();
    return new WorkScheduleMessages.UpdateResponse(
      Arrays.asList(new WorkScheduleEvents.UpdatedEvent(this.id)));
  }

  public WorkScheduleMessages.DeleteResponse apply(WorkScheduleMessages.DeleteRequest request) {
    return new WorkScheduleMessages.DeleteResponse(
      Arrays.asList(new WorkScheduleEvents.DeletedEvent(this.id)));
  }


}
