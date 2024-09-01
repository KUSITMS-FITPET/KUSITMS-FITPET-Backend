package fitpet_be.application.service;

import fitpet_be.application.dto.response.CardnewsDetailResponse;
import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.common.PageResponse;
import org.springframework.data.domain.Pageable;


public interface CardnewsService {

    PageResponse<CardnewsListResponse> getCardnewsListDesc(Pageable pageable);

    PageResponse<CardnewsListResponse> getCardnewsListAsc(Pageable pageable);

    CardnewsDetailResponse getCardnewsDetail(Long cardNewsId);

}
