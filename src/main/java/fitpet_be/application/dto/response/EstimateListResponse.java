package fitpet_be.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EstimateListResponse {

    private Long estimateId;
    private String estimateIP;
    private String estimateRefeere;
    private LocalDateTime createdAt;
    private String petInfo;
    private String petName;
    private Long petAge;
    private String petSpecies;
    private String moreInfo;
    private String phoneNumber;

    @Builder
    public EstimateListResponse(Long estimateId, String estimateIP,
                                String estimateRefeere, LocalDateTime createdAt,
                                String petInfo, String petName, Long petAge,
                                String petSpecies, String moreInfo, String phoneNumber) {

        this.estimateId = estimateId;
        this.estimateIP = estimateIP;
        this.estimateRefeere = estimateRefeere;
        this.createdAt = createdAt;
        this.petInfo = petInfo;
        this.petName = petName;
        this.petSpecies = petSpecies;
        this.petAge = petAge;
        this.moreInfo = moreInfo;
        this.phoneNumber = phoneNumber;

    }

}
