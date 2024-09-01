package fitpet_be.application.service;

import fitpet_be.application.dto.response.ReviewDetailsResponse;
import fitpet_be.application.dto.response.ReviewListResponse;
import fitpet_be.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    PageResponse<ReviewListResponse> getReviewListDesc(Pageable pageable);

    PageResponse<ReviewListResponse> getReviewListAsc(Pageable pageable);

    ReviewDetailsResponse getReviewDetails(Long reviewId);
}
