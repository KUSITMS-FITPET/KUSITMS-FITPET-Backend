package fitpet_be.application.service;

import fitpet_be.application.dto.request.TestServiceRequest;
import fitpet_be.application.dto.response.TestResponse;

public interface TestService {

    TestResponse getTestResponse(TestServiceRequest request);

}
