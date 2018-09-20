package pico.erp.work.schedule;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;
import pico.erp.work.schedule.category.data.WorkScheduleCategory;
import pico.erp.work.schedule.data.WorkScheduleId;

public interface WorkScheduleMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WorkScheduleId id;

    @NotNull
    WorkScheduleCategory category;

    @NotNull
    LocalDate date;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    boolean holiday;

    @NotNull
    List<WorkScheduleTime> times;

  }

  @Data
  class UpdateRequest {

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    boolean holiday;

    @NotNull
    List<WorkScheduleTime> times;

  }

  @Data
  class DeleteRequest {

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Value
  class UpdateResponse {

    Collection<Event> events;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;

  }
}
