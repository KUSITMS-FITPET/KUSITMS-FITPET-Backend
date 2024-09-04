package fitpet_be.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EstimateUpdateRequest {

    private String petInfo;
    private String petName;
    private Long petAge;
    private String petSpecies;
    private String moreInfo;
    private String phoneNumber;

    @Builder
    public EstimateUpdateRequest(String petInfo, String petName,
                                 Long petAge, String petSpecies,
                                 String moreInfo, String phoneNumber) {

        this.petInfo = petInfo;
        this.petName = petName;
        this.petAge = petAge;
        this.petSpecies = petSpecies;
        this.moreInfo = moreInfo;
        this.phoneNumber = phoneNumber;

    }

}
