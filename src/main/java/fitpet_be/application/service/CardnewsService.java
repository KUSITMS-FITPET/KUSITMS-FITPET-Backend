package fitpet_be.application.service;

import fitpet_be.application.dto.request.CardnewsListRequest;
import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.common.PageResponse;

public interface CardnewsService {

    PageResponse<CardnewsListResponse> getCardnewsListDesc(CardnewsListRequest request);

    PageResponse<CardnewsListResponse> getCardnewsListAsc(CardnewsListRequest request);

}
