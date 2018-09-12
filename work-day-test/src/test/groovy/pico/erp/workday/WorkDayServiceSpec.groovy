package pico.erp.workday

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.workday.category.data.WorkDayCategoryId
import pico.erp.workday.data.WorkDayId
import pico.erp.workday.data.WorkTimeData
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WorkDayServiceSpec extends Specification {

  @Autowired
  WorkDayService workDayService

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
    def exists = workDayService.exists(WorkDayId.from("global-2018-08-14"))

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 작업일 확인"() {
    when:
    def exists = workDayService.exists(WorkDayId.from("global-2018-08-16"))

    then:
    exists == false
  }

  def "아이디로 존재하는 작업일를 조회"() {
    when:
    def workDay = workDayService.get(WorkDayId.from("global-2018-08-15"))

    then:
    workDay.name == "광복절"
    workDay.holiday == true
  }

  def "아이디로 존재하지 않는 작업일를 조회"() {
    when:
    workDayService.get(WorkDayId.from("global-2018-08-16"))

    then:
    thrown(WorkDayExceptions.NotFoundException)
  }

  def "기간 생성과 지정한 iCal 에서 제공되는 정보가 반영된다"() {
    when:
    workDayService.generate(
      new WorkDayRequests.GenerateRequest(
        begin: LocalDate.parse("2018-08-01"),
        end: LocalDate.parse("2018-10-31")
      )
    )
    //def firstDay = workDayService.get(WorkDayId.from(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())))
    //def lastDay = workDayService.get(WorkDayId.from(LocalDate.now().plusMonths(2).with(TemporalAdjusters.lastDayOfMonth())))

    def firstDay = workDayService.get(WorkDayCategoryId.from("global"), LocalDate.parse("2018-08-01"))
    def lastDay = workDayService.get(WorkDayCategoryId.from("global"), LocalDate.parse("2018-10-31"))
    def 개천절 = workDayService.get(WorkDayCategoryId.from("global"), LocalDate.parse("2018-10-03"))
    then:
    firstDay.date == LocalDate.parse("2018-08-01")
    lastDay.date == LocalDate.parse("2018-10-31")
    개천절.holiday == true
  }

}
