package fitpet_be.presentation.controller;

import fitpet_be.application.dto.request.FaqListSearchRequest;
import fitpet_be.application.dto.response.FaqListResponse;
import fitpet_be.application.service.FaqService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faqs")
public class FaqController {

    private final FaqService faqService;


    @Operation(summary = "FAQ 목록 조회", description = "FAQ 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<FaqListResponse>> getFaqList(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "category", defaultValue = "0") Long category) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(faqService.getFaqList(category, pageable));

    }
    @Operation(summary = "FAQ 질문 검색", description = "FAQ를 질문(Question)으로 조회합니다"
            +"\ncategory: 카테고리 번호 // "
            +"\nkeyword: 검색어 // ")
    @PostMapping("/search")
    public ApiResponse<PageResponse<FaqListResponse>> getFaqListSearch(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestBody FaqListSearchRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return ApiResponse.onSuccess(faqService.getFaqListSearch(request, pageable));

    }

}
