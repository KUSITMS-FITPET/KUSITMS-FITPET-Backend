package fitpet_be.presentation.controller;

import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ApiResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/estimates")
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateService estimateService;
    @PostMapping()
    public ApiResponse<String> createEstimate(@RequestBody EstimateServiceRequest estimateServiceRequest)
        throws IOException {
        estimateService.createEstimateService(estimateServiceRequest);
        return ApiResponse.onSuccess("견적서가 성공적으로 생성 되었습니다.");
    }

}
