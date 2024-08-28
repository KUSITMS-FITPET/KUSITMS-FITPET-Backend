package fitpet_be.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestResponse {

    private String message;

    @Builder
    public TestResponse(String message) {
        this.message = message;
    }

}
