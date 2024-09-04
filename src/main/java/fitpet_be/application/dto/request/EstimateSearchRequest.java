package fitpet_be.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EstimateSearchRequest {

    private String startDate;
    private String endDate;
    private String refeere;
    private String petInfo;
    private String phoneNumber;

    @Builder
    public EstimateSearchRequest(String startDate, String endDate,
                                 String refeere, String petInfo,
                                 String phoneNumber) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.refeere = refeere;
        this.petInfo = petInfo;
        this.phoneNumber = phoneNumber;

    }

}
