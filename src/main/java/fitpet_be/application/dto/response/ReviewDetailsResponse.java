package fitpet_be.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewDetailsResponse {
    private String petSpecies;
    private String petInfo;
    private Long petAge;
    private Integer star;
    private String content;
    private LocalDateTime localDateTime;

    @Builder
    public ReviewDetailsResponse(String petSpecies, String petInfo,
        Long petAge, Integer star, String content, LocalDateTime localDateTime
    ) {
        this.petSpecies = petSpecies;
        this.petInfo = petInfo;
        this.petAge = petAge;
        this.star = star;
        this.content = content;
        this.localDateTime = localDateTime;
    }
}
