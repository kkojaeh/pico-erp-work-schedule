package pico.erp.workday;

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
import pico.erp.workday.data.WorkDayCategory;
import pico.erp.workday.data.WorkDayCategoryId;
import pico.erp.workday.data.WorkTimeData;
import pico.erp.workday.impl.WorkDayCategoryImpl;

@Slf4j
@SpringBootConfigs
public class WorkDayApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "work-day/application";

  public static final String CONFIG_NAME_PROPERTY = "spring.config.name=work-day/application";

  public static final Properties DEFAULT_PROPERTIES = new Properties();

  static {
    DEFAULT_PROPERTIES.put("spring.config.name", CONFIG_NAME);
  }

  public static SpringApplication application() {
    return new SpringApplicationBuilder(WorkDayApplication.class)
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
      .packageToScan("pico.erp.workday")
      .entity(ROLE.class)
      .build();
  }

  @Override
  public int getOrder() {
    return 3;
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
  public Role workDayAccessorRole() {
    return ROLE.WORK_DAY_ACCESSOR;
  }

  @Bean
  @Public
  public Role workDayManagerRole() {
    return ROLE.WORK_DAY_MANAGER;
  }

  @Bean
  @Public
  public WorkDayCategory globalWorkDayCategory() {
    return new WorkDayCategoryImpl(
      WorkDayCategoryId.from("global"),
      "일반 작업일",
      Arrays.asList(
        new WorkTimeData(LocalTime.parse("09:00"), LocalTime.parse("12:00")),
        new WorkTimeData(LocalTime.parse("13:00"), LocalTime.parse("18:00"))
      )
    );
  }

}
