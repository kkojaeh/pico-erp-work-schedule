package pico.erp.workday.jpa;

import java.io.Serializable;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Embeddable
@Data
@EqualsAndHashCode(of = {"begin", "end"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkTimeEmbeddable implements Serializable {

  LocalTime begin;

  LocalTime end;

}
