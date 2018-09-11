package pico.erp.workday;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

@RequiredArgsConstructor
public enum ROLE implements Role {

  WORK_DAY_MANAGER,

  WORK_DAY_ACCESSOR;


  @Id
  @Getter
  private final String id = name();

}
