package fitpet_be.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardnewsListRequest {

    private Integer page;

    private Integer size;

    @Builder
    public CardnewsListRequest(Integer page, Integer size) {

        this.page = page;
        this.size = (size == null || size < 1) ? 9 : size;

    }

}
