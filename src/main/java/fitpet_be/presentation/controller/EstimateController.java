package fitpet_be.presentation.controller;

import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ApiResponse;
import fitpet_be.infrastructure.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/estimates")
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateService estimateService;
    private final S3Service s3Service;
    @Operation(summary = "견적서 입력", description = "견적서 값을 입력합니다"
        + "\n petName: 강아지 이름 // "
        + "\n petInfo: 동물 정보(강아지 고양이) // "
        + "\n petSpecies: 동물 종류 // "
        + "\n petAge: 동물 나이 // "
        + "\n phoneNumber: 사용자 번호 // "
        + "\n moreInfo: 추가 정보 // "
        + "\n agreement: 동의서 여부(boolean) //")
    @PostMapping()
    public ApiResponse<String> createEstimate(@RequestBody EstimateServiceRequest estimateServiceRequest)
        throws IOException {

        estimateService.createEstimateService(estimateServiceRequest);

        return ApiResponse.onSuccess("견적서가 성공적으로 생성 되었습니다.");

    }


}
