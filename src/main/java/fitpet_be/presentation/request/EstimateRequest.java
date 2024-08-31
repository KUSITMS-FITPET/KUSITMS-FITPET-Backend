package fitpet_be.presentation.request;

import fitpet_be.application.dto.request.EstimateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EstimateRequest {
    private String petName;
    private String petInfo;
    private String petSpecies;
    private Long petAge;
    private String phoneNumber;
    private String moreInfo;
    private Boolean agreement;

    @Builder
    public EstimateRequest(String petName, String petInfo, String petSpecies,
        Long petAge, String phoneNumber, String moreInfo, Boolean agreement) {

        this.petName = petName;
        this.petInfo = petInfo;
        this.petSpecies = petSpecies;
        this.petAge = petAge;
        this.phoneNumber = phoneNumber;
        this.moreInfo = moreInfo;
        this.agreement = agreement;

    }
    public EstimateServiceRequest estimateServiceRequest() {

        return EstimateServiceRequest.builder()
            .petName(petName)
            .petInfo(petInfo)
            .petSpecies(petSpecies)
            .petAge(petAge)
            .phoneNumber(phoneNumber)
            .moreInfo(moreInfo)
            .agreement(agreement)
            .build();
    }
}
