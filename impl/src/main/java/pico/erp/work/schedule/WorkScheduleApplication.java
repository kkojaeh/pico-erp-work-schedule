package pico.erp.work.schedule;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import kkojaeh.spring.boot.component.Give;
import kkojaeh.spring.boot.component.SpringBootComponent;
import kkojaeh.spring.boot.component.SpringBootComponentBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pico.erp.shared.SharedConfiguration;
import pico.erp.shared.data.Role;
import pico.erp.work.schedule.WorkScheduleApi.Roles;
import pico.erp.work.schedule.category.WorkScheduleCategory;
import pico.erp.work.schedule.category.WorkScheduleCategory.WorkScheduleCategoryImpl;
import pico.erp.work.schedule.category.WorkScheduleCategoryId;
import pico.erp.work.schedule.time.WorkScheduleTimeData;


@Slf4j
@SpringBootComponent("work-schedule")
@EntityScan
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
@SpringBootApplication
@Import(value = {
  SharedConfiguration.class
})
public class WorkScheduleApplication {

  public static void main(String[] args) {
    new SpringBootComponentBuilder()
      .component(WorkScheduleApplication.class)
      .run(args);
  }

  @Bean
  @Give
  public WorkScheduleCategory globalWorkDayCategory() {
    return new WorkScheduleCategoryImpl(
      WorkScheduleCategoryId.from("global"),
      "일반 작업일",
      ZoneId.of("Asia/Seoul"),
      Arrays.asList(
        new WorkScheduleTimeData(LocalTime.parse("09:00"), LocalTime.parse("12:00")),
        new WorkScheduleTimeData(LocalTime.parse("13:00"), LocalTime.parse("18:00"))
      )
    );
  }


  @Bean
  @Give
  public Role workScheduleAccessorRole() {
    return Roles.WORK_SCHEDULE_ACCESSOR;
  }

  @Bean
  @Give
  public Role workScheduleManagerRole() {
    return Roles.WORK_SCHEDULE_MANAGER;
  }

}
