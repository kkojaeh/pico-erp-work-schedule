package pico.erp.work.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface WorkScheduleExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "work-schedule.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "work-schedule.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "work-schedule.category.not.found.exception")
  class CategoryNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "work-schedule.cannot.modify.exception")
  class CannotModifyException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "work-schedule.illegal.time.exception")
  class IllegalTimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

}
