package fitpet_be.presentation.controller;

import fitpet_be.application.dto.response.TestResponse;
import fitpet_be.application.service.TestService;
import fitpet_be.common.ApiResponse;
import fitpet_be.presentation.request.TestRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExController {

    private final TestService testService;

    @GetMapping("/test")
    public ApiResponse<TestResponse> getTestResponse(
            @RequestBody @Valid TestRequest request) {

        return ApiResponse.onSuccess(testService.getTestResponse(request.toServiceRequest()));
    }
}
