package fitpet_be.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FaqCategoryResponse {

    private Long id;
    private String name;

    @Builder
    public FaqCategoryResponse(Long id, String name) {

        this.id = id;
        this.name = name;

    }

}
