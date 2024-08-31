package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.CardnewsListRequest;
import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.service.CardnewsService;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Cardnews;
import fitpet_be.domain.repository.CardnewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardnewsServiceImpl implements CardnewsService {

    private final CardnewsRepository cardnewsRepository;

    @Override
    public PageResponse<CardnewsListResponse> getCardnewsListDesc(CardnewsListRequest request) {

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Cardnews> cardnewsList = cardnewsRepository.findAllByOrderByDesc(pageable);

        List<CardnewsListResponse> cardnewsListResponses = cardnewsList.stream()
                .map(cardnews -> CardnewsListResponse.builder()
                        .cardNewsId(cardnews.getId())
                        .cardNewsTitle(cardnews.getTitle())
                        .cardNewsTitle(cardnews.getContent())
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
    public PageResponse<CardnewsListResponse> getCardnewsListAsc(CardnewsListRequest request) {

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Cardnews> cardnewsList = cardnewsRepository.findAllByOrderByAsc(pageable);

        List<CardnewsListResponse> cardnewsListResponses = cardnewsList.stream()
                .map(cardnews -> CardnewsListResponse.builder()
                        .cardNewsId(cardnews.getId())
                        .cardNewsTitle(cardnews.getTitle())
                        .cardNewsTitle(cardnews.getContent())
                        .build())
                .toList();

        Long totalCount = cardnewsRepository.cardnewsTotalCount();


        return PageResponse.<CardnewsListResponse>builder()
                .listPageResponse(cardnewsListResponses)
                .totalCount(totalCount)
                .size(cardnewsListResponses.size())
                .build();

    }

}
