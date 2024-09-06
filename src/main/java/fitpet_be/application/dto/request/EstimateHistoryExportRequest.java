package fitpet_be.application.dto.request;

import fitpet_be.application.dto.HistoryExportInfoDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EstimateHistoryExportRequest {

    private List<Long> ids;

}
