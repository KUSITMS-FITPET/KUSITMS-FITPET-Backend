package fitpet_be.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewFilterRequest {

    private boolean dog;
    private boolean cat;
    private String orderBy;

}
