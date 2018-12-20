package pico.erp.work.schedule;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import pico.erp.audit.AuditApi;
import pico.erp.audit.AuditConfiguration;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.Public;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.data.Role;
import pico.erp.shared.impl.ApplicationImpl;
import pico.erp.work.schedule.WorkScheduleApi.Roles;
import pico.erp.work.schedule.category.WorkScheduleCategory;
import pico.erp.work.schedule.category.WorkScheduleCategory.WorkScheduleCategoryImpl;
import pico.erp.work.schedule.category.WorkScheduleCategoryId;
import pico.erp.work.schedule.time.WorkScheduleTimeData;

@Slf4j
@SpringBootConfigs
public class WorkScheduleApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "work-schedule/application";

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
      .entity(Roles.class)
      .build();
  }

  @Bean
  @Public
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

  @Override
  public Set<ApplicationId> getDependencies() {
    return Stream.of(
      AuditApi.ID
    ).collect(Collectors.toSet());
  }

  @Override
  public ApplicationId getId() {
    return WorkScheduleApi.ID;
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
    return Roles.WORK_SCHEDULE_ACCESSOR;
  }

  @Bean
  @Public
  public Role workScheduleManagerRole() {
    return Roles.WORK_SCHEDULE_MANAGER;
  }

}
