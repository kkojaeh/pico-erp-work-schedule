package pico.erp.work.schedule;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import pico.erp.audit.data.AuditConfiguration;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.Public;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.data.Role;
import pico.erp.shared.impl.ApplicationImpl;
import pico.erp.work.schedule.category.data.WorkScheduleCategory;
import pico.erp.work.schedule.category.data.WorkScheduleCategory.WorkScheduleCategoryImpl;
import pico.erp.work.schedule.category.data.WorkScheduleCategoryId;
import pico.erp.work.schedule.data.WorkScheduleTimeData;

@Slf4j
@SpringBootConfigs
public class WorkScheduleApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "work-schedule/application";

  public static final String CONFIG_NAME_PROPERTY = "spring.config.name=work-schedule/application";

  public static final Properties DEFAULT_PROPERTIES = new Properties();

  static {
    DEFAULT_PROPERTIES.put("spring.config.name", CONFIG_NAME);
  }

  public static SpringApplication application() {
    return new SpringApplicationBuilder(WorkScheduleApplication.class)
      .properties(DEFAULT_PROPERTIES)
      .web(false)
      .build();
  }

  public static void main(String[] args) {
    application().run(args);
  }

  @Bean
  @Public
  public AuditConfiguration auditConfiguration() {
    return AuditConfiguration.builder()
      .packageToScan("pico.erp.work.schedule")
      .entity(ROLE.class)
      .build();
  }

  @Override
  public int getOrder() {
    return 3;
  }

  @Bean
  @Public
  public WorkScheduleCategory globalWorkDayCategory() {
    return new WorkScheduleCategoryImpl(
      WorkScheduleCategoryId.from("global"),
      "일반 작업일",
      Arrays.asList(
        new WorkScheduleTimeData(LocalTime.parse("09:00"), LocalTime.parse("12:00")),
        new WorkScheduleTimeData(LocalTime.parse("13:00"), LocalTime.parse("18:00"))
      )
    );
  }

  @Override
  public boolean isWeb() {
    return false;
  }

  @Override
  public pico.erp.shared.Application start(String... args) {
    return new ApplicationImpl(application().run(args));
  }

  @Bean
  @Public
  public Role workScheduleAccessorRole() {
    return ROLE.WORK_SCHEDULE_ACCESSOR;
  }

  @Bean
  @Public
  public Role workScheduleManagerRole() {
    return ROLE.WORK_SCHEDULE_MANAGER;
  }

}
