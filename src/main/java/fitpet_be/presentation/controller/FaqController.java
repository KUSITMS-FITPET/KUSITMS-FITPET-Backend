package fitpet_be.presentation.controller;

import fitpet_be.application.dto.response.FaqListResponse;
import fitpet_be.application.service.FaqService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/faqs")
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    public ApiResponse<PageResponse<FaqListResponse>> getFaqList(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "category", defaultValue = "0") Long category) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(faqService.getFaqList(category, pageable));

    }

}
