package fitpet_be.application.service;

import fitpet_be.application.dto.request.ReviewFilterRequest;
import fitpet_be.application.dto.request.ReviewServiceRequest;
import fitpet_be.application.dto.response.ReviewDetailsResponse;
import fitpet_be.application.dto.response.ReviewListResponse;
import fitpet_be.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    PageResponse<ReviewListResponse> getReviewsFilter(ReviewFilterRequest request, Pageable pageable);

    ReviewDetailsResponse getReviewDetails(Long reviewId);

    String createReview(ReviewServiceRequest reviewServiceRequest);

}
