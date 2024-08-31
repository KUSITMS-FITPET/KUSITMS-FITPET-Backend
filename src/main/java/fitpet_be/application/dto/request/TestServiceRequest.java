package fitpet_be.application.dto.request;

import fitpet_be.domain.model.Test;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestServiceRequest {

    private String test;

    @Builder
    public TestServiceRequest(String test) {

        this.test = test;

    }

    public Test toEntity() {

        return Test.builder()
                .test(test)
                .build();

    }

}
