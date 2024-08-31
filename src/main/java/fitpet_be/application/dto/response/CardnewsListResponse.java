package fitpet_be.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardnewsListResponse {

    private Long cardNewsId;
    private String cardNewsTitle;
    private String cardNewsContent;
    private String image_url;

    @Builder
    public CardnewsListResponse(Long cardNewsId, String cardNewsTitle,
                                String cardNewsContent, String image_url) {

        this.cardNewsId = cardNewsId;
        this.cardNewsTitle = cardNewsTitle;
        this.cardNewsContent = cardNewsContent;
        this.image_url = image_url;

    }

}
