package fitpet_be.presentation.request;

import fitpet_be.application.dto.request.TestServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestRequest {

    @NotBlank(message = "test는 필수입니다.")
    private String test;

    @Builder
    public TestRequest(String test) {
        this.test = test;
    }

    public TestServiceRequest toServiceRequest() {

        return TestServiceRequest.builder()
                .test(test)
                .build();
    }

}
