package fitpet_be.presentation.controller;

import fitpet_be.application.dto.request.ReviewServiceRequest;
import fitpet_be.application.dto.response.ReviewDetailsResponse;
import fitpet_be.application.dto.response.ReviewListResponse;
import fitpet_be.application.service.ReviewService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    @Operation(summary = "리뷰 조회", description = "리뷰를 최신 순으로 조회합니다")
    @GetMapping("/desc")
    public ApiResponse<PageResponse<ReviewListResponse>> getReviewsDesc(
        @RequestParam("page") int page,
        @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(reviewService.getReviewListDesc(pageable));

    }

    @Operation(summary = "리뷰 조회", description = "리뷰를 오래된 순으로 조회합니다")
    @GetMapping("/asc")
    public ApiResponse<PageResponse<ReviewListResponse>> getReviewsAsc(
        @RequestParam("page") int page,
        @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(reviewService.getReviewListAsc(pageable));

    }

    @Operation(summary = "리뷰 상세페이지 조회", description = "특정 리뷰의 상세페이지를 조회합니다")
    @Parameter(name = "reviewId", description = "리뷰 ID", required = true, example = "1")
    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewDetailsResponse> getReviewDetails(@PathVariable("reviewId") Long reviewId) {

        return ApiResponse.onSuccess(reviewService.getReviewDetails(reviewId));

    }

    @Operation(summary = "리뷰 작성", description = "새로운 리뷰를 작성합니다"
        + "\n petInfo: 동물 정보(강아지 고양이) // "
        + "\n petSpecies: 동물 종류 // "
        + "\n petAge: 동물 나이 // "
        + "\n content: 리뷰 내용 // "
        + "\n star: 별점(integer) //")
    @PostMapping()
    public ApiResponse<String> createReview(@RequestBody ReviewServiceRequest reviewServiceRequest){

        return ApiResponse.onSuccess(reviewService.createReview(reviewServiceRequest));

    }
}
