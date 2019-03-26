package pico.erp.work.schedule;

import java.util.LinkedList;
import java.util.List;
import kkojaeh.spring.boot.component.SpringBootComponentReadyEvent;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@Profile({"test-data"})
public class TestDataInitializer implements ApplicationListener<SpringBootComponentReadyEvent> {

  @Lazy
  @Autowired
  private WorkScheduleService workScheduleService;


  @Autowired
  private DataProperties dataProperties;

  @Override
  public void onApplicationEvent(SpringBootComponentReadyEvent event) {
    dataProperties.workSchedules.forEach(workScheduleService::create);
  }

  @Data
  @Configuration
  @ConfigurationProperties("data")
  public static class DataProperties {

    List<WorkScheduleRequests.CreateRequest> workSchedules = new LinkedList<>();

  }

}
