package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.ReviewServiceRequest;
import fitpet_be.application.dto.response.ReviewDetailsResponse;
import fitpet_be.application.dto.response.ReviewListResponse;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.ReviewService;
import fitpet_be.common.ErrorStatus;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Review;
import fitpet_be.domain.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public PageResponse<ReviewListResponse> getReviewListDesc(Pageable pageable) {
        Page<Review> reviewsList = reviewRepository.findAllByOrderByDesc(pageable);

        return getReviewListResponsePageResponse(reviewsList);
    }

    @Override
    public PageResponse<ReviewListResponse> getReviewListAsc(Pageable pageable) {
        Page<Review> reviewsList = reviewRepository.findAllByOrderByAsc(pageable);

        return getReviewListResponsePageResponse(reviewsList);
    }

    private PageResponse<ReviewListResponse> getReviewListResponsePageResponse(
        Page<Review> reviewsList) {
        List<ReviewListResponse> reviewListResponses = reviewsList.stream()
            .map(review -> ReviewListResponse.builder()
                .reviewId(review.getId())
                .petSpecies(review.getPetSpecies())
                .petInfo(review.getPetInfo())
                .star(review.getStar())
                .content(review.getPetInfo())
                .createdAt(review.getCreatedAt())
                .build())
            .toList();

        Long totalCount = reviewRepository.reviewsTotalCount();

        return PageResponse.<ReviewListResponse>builder()
            .listPageResponse(reviewListResponses)
            .totalCount(totalCount)
            .size(reviewListResponses.size())
            .build();
    }

    @Override
    public ReviewDetailsResponse getReviewDetails(Long reviewId) {
        Review review = getReview(reviewId);


        return ReviewDetailsResponse.builder()
            .petSpecies(review.getPetSpecies())
            .petInfo(review.getPetInfo())
            .petAge(review.getPetAge())
            .star(review.getStar())
            .content(review.getContent())
            .localDateTime(review.getCreatedAt())
            .build();

    }

    @Override
    @Transactional
    public String createReview(ReviewServiceRequest reviewServiceRequest) {

        Review review = Review.builder()
            .petInfo(reviewServiceRequest.getPetInfo())
            .petAge(reviewServiceRequest.getPetAge())
            .petSpecies(reviewServiceRequest.getPetSpecies())
            .content(reviewServiceRequest.getContent())
            .star(reviewServiceRequest.getStar())
            .build();

        reviewRepository.save(review);

        return "리뷰가 정상적으로 등록되었습니다";
    }

    private Review getReview(Long reviewId) {

        return reviewRepository.findById(reviewId).orElseThrow(
            () -> new ApiException(ErrorStatus._REVEIEW_NOT_FOUND)
        );

    }
}
