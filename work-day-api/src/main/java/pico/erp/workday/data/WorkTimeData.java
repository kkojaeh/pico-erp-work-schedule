package pico.erp.workday.data;

import java.io.Serializable;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"begin", "end"})
public class WorkTimeData implements Serializable {

  private static final long serialVersionUID = 1L;

  LocalTime begin;

  LocalTime end;

}
