package fitpet_be.application.dto.request;

import fitpet_be.domain.model.Estimate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class EstimateServiceRequest {
    private String petName;
    private String petInfo;
    private String petSpecies;
    private Long petAge;
    private String phoneNumber;
    private String moreInfo;
    private Boolean agreement;


    @Builder
    public EstimateServiceRequest(String petName, String petInfo, String petSpecies,
        Long petAge, String phoneNumber, String moreInfo, Boolean agreement) {

        this.petName = petName;
        this.petInfo = petInfo;
        this.petSpecies = petSpecies;
        this.petAge = petAge;
        this.phoneNumber = phoneNumber;
        this.moreInfo = moreInfo;
        this.agreement = agreement;

    }

    public Estimate toEntity(EstimateServiceRequest estimateServiceRequest) {
        return Estimate.builder()
            .petInfo(estimateServiceRequest.getPetInfo())
            .petName(estimateServiceRequest.getPetName())
            .petAge(estimateServiceRequest.getPetAge())
            .petSpecies(estimateServiceRequest.getPetSpecies())
            .phoneNumber(estimateServiceRequest.getPhoneNumber())
            .moreInfo(estimateServiceRequest.getMoreInfo())
            .agreement(estimateServiceRequest.getAgreement())
            .build();
    }
}
