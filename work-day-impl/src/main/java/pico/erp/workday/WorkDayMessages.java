package pico.erp.workday;

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
import pico.erp.workday.category.data.WorkDayCategory;
import pico.erp.workday.data.WorkDayId;

public interface WorkDayMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WorkDayId id;

    @NotNull
    WorkDayCategory category;

    @NotNull
    LocalDate date;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    boolean holiday;

    @NotNull
    List<WorkTime> times;

  }

  @Data
  class UpdateRequest {

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    boolean holiday;

    @NotNull
    List<WorkTime> times;

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
