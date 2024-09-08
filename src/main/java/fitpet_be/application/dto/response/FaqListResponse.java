package fitpet_be.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FaqListResponse {

    private List<FaqCategoryResponse> categories;
    private List<FaqFaqsResponse> faqs;

    @Builder
    public FaqListResponse(List<FaqCategoryResponse> categories,
                           List<FaqFaqsResponse> faqs) {

        this.categories = categories;
        this.faqs = faqs;

    }

}
