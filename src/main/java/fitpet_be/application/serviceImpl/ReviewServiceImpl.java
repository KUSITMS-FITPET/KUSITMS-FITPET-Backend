package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.response.ReviewListResponse;
import fitpet_be.application.service.ReviewService;
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

}
