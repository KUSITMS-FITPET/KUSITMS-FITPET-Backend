package fitpet_be.application.request;

import fitpet_be.domain.model.Estimate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EstimateRequest {
    private String petName;
    private String petInfo;
    private String petSpecies;
    private Long petAge;
    private String phoneNumber;
    private String moreInfo;
    private Boolean agreement;


    public Estimate toEntity(EstimateRequest estimateRequest) {
        return Estimate.builder()
            .petInfo(estimateRequest.getPetInfo())
            .petName(estimateRequest.getPetName())
            .petAge(estimateRequest.getPetAge())
            .petSpecies(estimateRequest.getPetSpecies())
            .phoneNumber(estimateRequest.getPhoneNumber())
            .moreInfo(estimateRequest.getMoreInfo())
            .agreement(estimateRequest.getAgreement())
            .build();
    }
}
