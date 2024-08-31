package fitpet_be.presentation.controller;

import fitpet_be.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    @GetMapping("/desc")
    public ApiResponse<String> ExampleController(@PathVariable Long testId) {
        return ApiResponse.onSuccess(testId + "견적서가 직성되었습니다.");
    }
}
