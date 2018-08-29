package pico.erp.workday;

import java.util.List;
import javax.validation.constraints.NotNull;
import pico.erp.shared.data.LabeledValuable;
import pico.erp.workday.data.WorkDayView;

public interface WorkDayQuery {

  List<? extends LabeledValuable> asCategoryLabels();

  List<WorkDayView> retrieve(@NotNull WorkDayView.Filter filter);

}
