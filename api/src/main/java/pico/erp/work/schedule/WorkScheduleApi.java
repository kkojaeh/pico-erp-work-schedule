package pico.erp.work.schedule;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

public final class WorkScheduleApi {

  @RequiredArgsConstructor
  public enum Roles implements Role {

    WORK_SCHEDULE_MANAGER,

    WORK_SCHEDULE_ACCESSOR;

    @Id
    @Getter
    private final String id = name();

  }
}
