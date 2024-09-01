package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.FaqListSearchRequest;
import fitpet_be.application.dto.response.FaqListResponse;
import fitpet_be.application.service.FaqService;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Faq;
import fitpet_be.domain.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    public PageResponse<FaqListResponse> getFaqList(Long category, Pageable pageable) {

        Page<Faq> faqs = faqRepository.findAllByCategory(category, pageable);
        
        Long totalCount = faqRepository.faqTotalCountByCategory(category);

        return getFaqListPageResponse(faqs, totalCount);

    }

    @Override
    public PageResponse<FaqListResponse> getFaqListSearch(FaqListSearchRequest request, Pageable pageable) {

        Page<Faq> faqs = faqRepository.searchAllByKeywordAndCategory(request.getKeyword(), request.getCategory(), pageable);

        Long totalCount = faqRepository.faqTotalCountByCategory(request.getCategory());

        return getFaqListPageResponse(faqs, totalCount);

    }

    private PageResponse<FaqListResponse> getFaqListPageResponse(Page<Faq> faqs, Long totalCount) {

        List<FaqListResponse> faqListResponses = faqs.stream()
                .map(faq -> FaqListResponse.builder()
                        .category(faq.getCategory())
                        .question(faq.getQuestion())
                        .answer(faq.getAnswer())
                        .build())
                .toList();

        return PageResponse.<FaqListResponse>builder()
                .listPageResponse(faqListResponses)
                .totalCount(totalCount)
                .size(faqListResponses.size())
                .build();

    }
    
}
