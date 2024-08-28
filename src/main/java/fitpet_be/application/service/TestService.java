package fitpet_be.application.service;

import fitpet_be.application.request.TestServiceRequest;
import fitpet_be.application.response.TestResponse;

public interface TestService {

    TestResponse getTestResponse(TestServiceRequest request);

}
