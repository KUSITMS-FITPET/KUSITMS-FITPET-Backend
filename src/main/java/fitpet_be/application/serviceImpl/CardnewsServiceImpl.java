package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.response.CardnewsDetailResponse;
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

    private final CardnewsRepository cardnewsRepository;

    @Override
    public PageResponse<CardnewsListResponse> getCardnewsListDesc(Pageable pageable) {

        Page<Cardnews> cardnewsList = cardnewsRepository.findAllByOrderByDesc(pageable);

        return getCardnewsListResponsePageResponse(cardnewsList);
    }

    @Override
    public PageResponse<CardnewsListResponse> getCardnewsListAsc(Pageable pageable) {

        Page<Cardnews> cardnewsList = cardnewsRepository.findAllByOrderByAsc(pageable);

        return getCardnewsListResponsePageResponse(cardnewsList);

    }

    private PageResponse<CardnewsListResponse> getCardnewsListResponsePageResponse(
        Page<Cardnews> cardnewsList) {
        List<CardnewsListResponse> cardnewsListResponses = cardnewsList.stream()
                .map(cardnews -> CardnewsListResponse.builder()
                        .cardNewsId(cardnews.getId())
                        .cardNewsTitle(cardnews.getTitle())
                        .cardNewsTitle(cardnews.getContent())
                        .image_url(cardnews.getImageUrl())
                        .build())
                .toList();

        Long totalCount = cardnewsRepository.cardnewsTotalCount();

        return PageResponse.<CardnewsListResponse>builder()
                .listPageResponse(cardnewsListResponses)
                .totalCount(totalCount)
                .size(cardnewsListResponses.size())
                .build();
    }

    @Override
    public CardnewsDetailResponse getCardnewsDetail(Long cardNewsId) {

        Cardnews cardnews = cardnewsRepository.findById(cardNewsId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드뉴스입니다."));

        return CardnewsDetailResponse.builder()
                .cardNewsId(cardnews.getId())
                .cardNewsTitle(cardnews.getTitle())
                .cardNewsContent((cardnews.getContent()))
                .image_url(cardnews.getImageUrl())
                .build();

    }

}
