package fitpet_be.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FaqListResponse {

    private Long category;
    private String question;
    private String answer;

    @Builder
    public FaqListResponse (Long category, String question, String answer) {

        this.category = category;
        this.question = question;
        this.answer = answer;

    }

}
