package fitpet_be.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewServiceRequest {
    private String petInfo;
    private Long petAge;
    private String petSpecies;
    private String content;
    private Integer star;

    @Builder
    public ReviewServiceRequest(String petInfo, Long petAge, String petSpecies, String content, Integer star) {

        this.petInfo = petInfo;
        this.petAge = petAge;
        this.petSpecies = petSpecies;
        this.content = content;
        this.star = star;

    }
}
