package pico.erp.workday;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;
import pico.erp.workday.category.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkDayId;
import pico.erp.workday.data.WorkTimeData;

public interface WorkDayRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WorkDayId id;

    @Valid
    @NotNull
    WorkDayCategoryId categoryId;

    @NotNull
    LocalDate date;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    boolean holiday;

    @NotNull
    List<WorkTimeData> times;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class GenerateRequest {

    LocalDate begin;

    LocalDate end;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    WorkDayId id;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    boolean holiday;

    @NotNull
    List<WorkTimeData> times;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WorkDayId id;
  }
}
