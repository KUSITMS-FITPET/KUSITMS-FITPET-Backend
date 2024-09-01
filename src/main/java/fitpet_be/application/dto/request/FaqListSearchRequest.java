package fitpet_be.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FaqListSearchRequest {

    private Long category;
    private String keyword;

    @Builder
    public FaqListSearchRequest(Long category, String keyword) {

        this.category = category;
        this.keyword = keyword;

    }

}
