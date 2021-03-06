package pico.erp.work.schedule;

import java.util.List;
import javax.validation.constraints.NotNull;
import pico.erp.shared.data.LabeledValuable;

public interface WorkScheduleQuery {

  List<? extends LabeledValuable> asCategoryLabels();

  List<WorkScheduleView> retrieve(@NotNull WorkScheduleView.Filter filter);

}
