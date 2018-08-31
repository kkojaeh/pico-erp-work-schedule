package pico.erp.workday

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.workday.data.WorkDayCategoryId
import pico.erp.workday.data.WorkDayId
import pico.erp.workday.data.WorkDayView
import pico.erp.workday.data.WorkTimeData
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WorkDayQuerySpec extends Specification {

  @Autowired
  WorkDayService workDayService

  @Autowired
  WorkDayQuery workDayQuery

  def setup() {
    workDayService.create(new WorkDayRequests.CreateRequest(
      id: WorkDayId.from("global-2018-08-14"),
      date: LocalDate.parse("2018-08-14"),
      categoryId: WorkDayCategoryId.from("global"),
      holiday: false,
      times: new LinkedList<WorkTimeData>()
    ))
    workDayService.create(new WorkDayRequests.CreateRequest(
      id: WorkDayId.from("global-2018-08-15"),
      date: LocalDate.parse("2018-08-15"),
      categoryId: WorkDayCategoryId.from("global"),
      name: "광복절",
      holiday: true,
      times: new LinkedList<WorkTimeData>()
    ))
  }

  def "아이디로 존재하는 작업일 확인"() {
    when:
    def result = workDayQuery.retrieve(new WorkDayView.Filter(
      categoryId: WorkDayCategoryId.from("global"),
      begin: LocalDate.parse("2018-08-15"),
      end: LocalDate.parse("2018-08-15")
    ))

    then:
    result.size() == 1
  }

}
