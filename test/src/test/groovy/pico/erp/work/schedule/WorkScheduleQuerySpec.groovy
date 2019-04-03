package pico.erp.work.schedule

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.work.schedule.category.WorkScheduleCategoryId
import pico.erp.work.schedule.time.WorkScheduleTimeData
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(classes = [WorkScheduleApplication])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WorkScheduleQuerySpec extends Specification {

  @Autowired
  WorkScheduleService workScheduleService

  @Autowired
  WorkScheduleQuery workScheduleQuery

  def setup() {
    workScheduleService.create(new WorkScheduleRequests.CreateRequest(
      id: WorkScheduleId.from("global-2018-08-14"),
      date: LocalDate.parse("2018-08-14"),
      categoryId: WorkScheduleCategoryId.from("global"),
      holiday: false,
      times: new LinkedList<WorkScheduleTimeData>()
    ))
    workScheduleService.create(new WorkScheduleRequests.CreateRequest(
      id: WorkScheduleId.from("global-2018-08-15"),
      date: LocalDate.parse("2018-08-15"),
      categoryId: WorkScheduleCategoryId.from("global"),
      name: "광복절",
      holiday: true,
      times: new LinkedList<WorkScheduleTimeData>()
    ))
  }

  def "아이디로 존재하는 작업일 확인"() {
    when:
    def result = workScheduleQuery.retrieve(new WorkScheduleView.Filter(
      categoryId: WorkScheduleCategoryId.from("global"),
      begin: LocalDate.parse("2018-08-15"),
      end: LocalDate.parse("2018-08-15")
    ))

    then:
    result.size() == 1
  }

}
