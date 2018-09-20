package pico.erp.work.schedule

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.work.schedule.category.data.WorkScheduleCategoryId
import pico.erp.work.schedule.data.WorkScheduleId
import pico.erp.work.schedule.data.WorkScheduleTimeData
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WorkScheduleServiceSpec extends Specification {

  @Autowired
  WorkScheduleService workScheduleService

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
    def exists = workScheduleService.exists(WorkScheduleId.from("global-2018-08-14"))

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 작업일 확인"() {
    when:
    def exists = workScheduleService.exists(WorkScheduleId.from("global-2018-08-16"))

    then:
    exists == false
  }

  def "아이디로 존재하는 작업일를 조회"() {
    when:
    def workSchedule = workScheduleService.get(WorkScheduleId.from("global-2018-08-15"))

    then:
    workSchedule.name == "광복절"
    workSchedule.holiday == true
  }

  def "아이디로 존재하지 않는 작업일를 조회"() {
    when:
    workScheduleService.get(WorkScheduleId.from("global-2018-08-16"))

    then:
    thrown(WorkScheduleExceptions.NotFoundException)
  }

  def "기간 생성과 지정한 iCal 에서 제공되는 정보가 반영된다"() {
    when:
    workScheduleService.generate(
      new WorkScheduleRequests.GenerateRequest(
        begin: LocalDate.parse("2018-08-01"),
        end: LocalDate.parse("2018-10-31")
      )
    )
    //def firstDay = workScheduleService.get(WorkScheduleId.from(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())))
    //def lastDay = workScheduleService.get(WorkScheduleId.from(LocalDate.now().plusMonths(2).with(TemporalAdjusters.lastDayOfMonth())))

    def firstDay = workScheduleService.get(WorkScheduleCategoryId.from("global"), LocalDate.parse("2018-08-01"))
    def lastDay = workScheduleService.get(WorkScheduleCategoryId.from("global"), LocalDate.parse("2018-10-31"))
    def 개천절 = workScheduleService.get(WorkScheduleCategoryId.from("global"), LocalDate.parse("2018-10-03"))
    then:
    firstDay.date == LocalDate.parse("2018-08-01")
    lastDay.date == LocalDate.parse("2018-10-31")
    개천절.holiday == true
  }

}
