package fitpet_be.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FaqFaqsResponse {

    private Long categoryId;
    private String question;
    private String answer;

    @Builder
    public FaqFaqsResponse(Long categoryId, String question,
                           String answer) {

        this.categoryId = categoryId;
        this.question = question;
        this.answer = answer;

    }

}
