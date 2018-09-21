package pico.erp.work.schedule;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;
import pico.erp.work.schedule.category.data.WorkScheduleCategoryId;
import pico.erp.work.schedule.data.WorkScheduleId;
import pico.erp.work.schedule.data.WorkScheduleTimeData;

public interface WorkScheduleRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WorkScheduleId id;

    @Valid
    @NotNull
    WorkScheduleCategoryId categoryId;

    @NotNull
    LocalDate date;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    boolean holiday;

    @NotNull
    List<WorkScheduleTimeData> times;

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
    WorkScheduleId id;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    boolean holiday;

    @NotNull
    List<WorkScheduleTimeData> times;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WorkScheduleId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CalculateEndRequest {

    @Valid
    @NotNull
    WorkScheduleCategoryId categoryId;

    @NotNull
    OffsetDateTime begin;

    @Min(0)
    long durationMinutes;


  }
}
