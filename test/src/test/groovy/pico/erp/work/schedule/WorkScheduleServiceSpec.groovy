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
import java.time.LocalTime
import java.time.OffsetDateTime

@SpringBootTest(classes = [WorkScheduleApplication])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WorkScheduleServiceSpec extends Specification {

  @Autowired
  WorkScheduleService workScheduleService


  def id = WorkScheduleId.from("global-2018-08-14")

  def unknownId = WorkScheduleId.from("unknown")

  def categoryId = WorkScheduleCategoryId.from("global")

  def setup() {
    workScheduleService.create(new WorkScheduleRequests.CreateRequest(
      id: id,
      date: LocalDate.parse("2018-08-14"),
      categoryId: categoryId,
      holiday: false,
      times: new LinkedList<WorkScheduleTimeData>()
    ))
    workScheduleService.create(new WorkScheduleRequests.CreateRequest(
      id: WorkScheduleId.from("global-2018-08-15"),
      date: LocalDate.parse("2018-08-15"),
      categoryId: categoryId,
      name: "광복절",
      holiday: true,
      times: new LinkedList<WorkScheduleTimeData>()
    ))
  }

  def "시작시간이 종료시간보다 이후라면 오류 발생"() {
    when:
    workScheduleService.create(new WorkScheduleRequests.CreateRequest(
      id: WorkScheduleId.from("global-2017-08-13"),
      date: LocalDate.parse("2017-08-13"),
      categoryId: categoryId,
      holiday: false,
      times: Arrays.asList(
        new WorkScheduleTimeData(
          begin: LocalTime.parse("11:00"),
          end: LocalTime.parse("09:00")
        )
      )
    ))

    then:
    thrown(WorkScheduleExceptions.IllegalTimeException)
  }

  def "아이디로 존재하는 작업일 확인"() {
    when:
    def exists = workScheduleService.exists(id)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 작업일 확인"() {
    when:
    def exists = workScheduleService.exists(unknownId)

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

    def firstDay = workScheduleService.get(categoryId, LocalDate.parse("2018-08-01"))
    def lastDay = workScheduleService.get(categoryId, LocalDate.parse("2018-10-31"))
    def 개천절 = workScheduleService.get(categoryId, LocalDate.parse("2018-10-03"))
    then:
    firstDay.date == LocalDate.parse("2018-08-01")
    lastDay.date == LocalDate.parse("2018-10-31")
    개천절.holiday == true
  }

  def "작업 종료시간 계산시 시작시간이 작업시간이 아니면 오류"() {
    when:
    def end = workScheduleService.calculateEnd(
      new WorkScheduleRequests.CalculateEndRequest(
        categoryId: categoryId,
        begin: OffsetDateTime.parse("2018-08-11T08:00:00+09:00"),
        durationMinutes: 60 * 12
      )
    )

    then:
    thrown(WorkScheduleExceptions.IllegalTimeException)
  }

  def "작업 종료시간 계산시 지정된 작업시간에서만 차감하여 시간을 계산한다"() {
    when:
    def end = workScheduleService.calculateEnd(
      new WorkScheduleRequests.CalculateEndRequest(
        categoryId: categoryId,
        begin: OffsetDateTime.parse("2018-08-11T09:00:00+09:00"),
        durationMinutes: 60 * 12 // 12 시간
      )
    )
    // 2018-08-11T09:00 ~ 2018-08-11T12:00 3시간
    // 2018-08-11T13:00 ~ 2018-08-11T18:00 5시간
    // 2018-08-12T09:00 ~ 2018-08-12T12:00 3시간
    // 2018-08-12T13:00 ~ 2018-08-12T14:00 1시간

    then:
    end == OffsetDateTime.parse("2018-08-12T14:00:00+09:00")
  }

  def "작업 종료시간 계산시 시작 시간이 정시가 아닐 수 있다"() {
    when:
    def end = workScheduleService.calculateEnd(
      new WorkScheduleRequests.CalculateEndRequest(
        categoryId: categoryId,
        begin: OffsetDateTime.parse("2018-08-11T10:30:00+09:00"),
        durationMinutes: 60 * 12 // 12 시간
      )
    )
    // 2018-08-11T10:30 ~ 2018-08-11T12:00 1.5시간
    // 2018-08-11T13:00 ~ 2018-08-11T18:00 5시간
    // 2018-08-12T09:00 ~ 2018-08-12T12:00 3시간
    // 2018-08-12T13:00 ~ 2018-08-12T15:30 2.5시간

    then:
    end == OffsetDateTime.parse("2018-08-12T15:30:00+09:00")
  }

}
