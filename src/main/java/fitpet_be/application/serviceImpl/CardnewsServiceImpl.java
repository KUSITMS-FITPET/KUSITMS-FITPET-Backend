package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.service.CardnewsService;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Cardnews;
import fitpet_be.domain.repository.CardnewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardnewsServiceImpl implements CardnewsService {

    private final CardnewsRepository cardNewsRepository;

    @Override
    public PageResponse<CardnewsListResponse> getCardnewsListDesc(Pageable pageable) {

        Page<Cardnews> cardNewsList = cardNewsRepository.findAllByOrderByDesc(pageable);

        return getCardnewsListResponsePageResponse(cardNewsList);
    }

    @Override
    public PageResponse<CardnewsListResponse> getCardnewsListAsc(Pageable pageable) {

        Page<Cardnews> cardNewsList = cardNewsRepository.findAllByOrderByAsc(pageable);

        return getCardnewsListResponsePageResponse(cardNewsList);

    }


    private PageResponse<CardnewsListResponse> getCardnewsListResponsePageResponse(
            Page<Cardnews> cardNewsList) {
        List<CardnewsListResponse> cardNewsListResponses = cardNewsList.stream()
                .map(cardNews -> CardnewsListResponse.builder()
                        .cardNewsId(cardNews.getId())
                        .cardNewsTitle(cardNews.getTitle())
                        .cardNewsContent(cardNews.getContent())
                        .cardNewsContentDetail((cardNews.getContentDetails()))
                        .image_url(cardNews.getImageUrl())
                        .build())
                .toList();

        Long totalCount = cardNewsRepository.cardNewsTotalCount();

        return PageResponse.<CardnewsListResponse>builder()
                .listPageResponse(cardNewsListResponses)
                .totalCount(totalCount)
                .size(cardNewsListResponses.size())
                .build();
    }

}
