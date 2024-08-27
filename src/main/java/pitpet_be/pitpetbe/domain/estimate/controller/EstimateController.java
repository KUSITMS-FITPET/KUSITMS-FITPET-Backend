package pitpet_be.pitpetbe.domain.estimate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pitpet_be.pitpetbe.global.response.ApiResponse;

@RestController
@RequestMapping("/estimates")
@RequiredArgsConstructor
public class EstimateController {
    @GetMapping("/{testId}")
    public ApiResponse<String> ExampleController(@PathVariable Long testId) {
        return ApiResponse.onSuccess(testId + "견적서가 직성되었습니다.");
    }

}
