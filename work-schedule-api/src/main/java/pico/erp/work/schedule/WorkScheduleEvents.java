package pico.erp.work.schedule;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;
import pico.erp.work.schedule.data.WorkScheduleId;

public interface WorkScheduleEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.work-schedule.created";

    private WorkScheduleId workScheduleId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.work-schedule.deleted";

    private WorkScheduleId workScheduleId;

    public String channel() {
      return CHANNEL;
    }


  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.work-schedule.updated";

    private WorkScheduleId workScheduleId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class GeneratedEvent implements Event {

    public final static String CHANNEL = "event.work-schedule.generated";

    LocalDate begin;

    LocalDate end;

    public String channel() {
      return CHANNEL;
    }

  }
}
