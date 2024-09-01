package fitpet_be.application.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EstimateExportRequest {
    private List<Long> ids;

    @Builder
    public EstimateExportRequest(List<Long> ids) {
        this.ids = ids;
    }
}
