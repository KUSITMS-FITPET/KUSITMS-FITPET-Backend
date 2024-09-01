package fitpet_be.presentation.controller;

import fitpet_be.application.dto.response.ReviewDetailsResponse;
import fitpet_be.application.dto.response.ReviewListResponse;
import fitpet_be.application.service.ReviewService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    @GetMapping("/desc")
    public ApiResponse<PageResponse<ReviewListResponse>> getReviewsDesc(
        @RequestParam("page") int page,
        @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(reviewService.getReviewListDesc(pageable));

    }

    @GetMapping("/asc")
    public ApiResponse<PageResponse<ReviewListResponse>> getReviewsAsc(
        @RequestParam("page") int page,
        @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(reviewService.getReviewListAsc(pageable));

    }

    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewDetailsResponse> getReviewDetails(@PathVariable("reviewId") Long reviewId) {

        return ApiResponse.onSuccess(reviewService.getReviewDetails(reviewId));

    }
}
