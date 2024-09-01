package fitpet_be.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewListResponse {
    private Long reviewId;
    private String petSpecies;
    private String petInfo;
    private Integer star;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public ReviewListResponse(Long reviewId, String petSpecies,
        String petInfo, Integer star, String content, LocalDateTime createdAt) {

        this.reviewId = reviewId;
        this.petSpecies = petSpecies;
        this.petInfo = petInfo;
        this.star = star;
        this.content = content;
        this.createdAt = createdAt;

    }
}
