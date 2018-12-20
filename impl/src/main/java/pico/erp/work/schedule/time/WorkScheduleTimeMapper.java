package pico.erp.work.schedule.time;

import org.mapstruct.Mapper;

@Mapper
public abstract class WorkScheduleTimeMapper {

  public WorkScheduleTime domain(WorkScheduleTimeEmbeddable data) {
    return WorkScheduleTime.builder()
      .begin(data.getBegin())
      .end(data.getEnd())
      .build();
  }

  public abstract WorkScheduleTimeEmbeddable entity(WorkScheduleTime workScheduleTime);

  public abstract WorkScheduleTimeData map(WorkScheduleTime workScheduleTime);

  public WorkScheduleTime map(WorkScheduleTimeData data) {
    return WorkScheduleTime.builder()
      .begin(data.getBegin())
      .end(data.getEnd())
      .build();
  }


}
