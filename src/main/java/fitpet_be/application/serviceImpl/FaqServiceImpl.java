package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.dto.response.FaqListResponse;
import fitpet_be.application.service.FaqService;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Cardnews;
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

        List<FaqListResponse> faqListResponses = faqs.stream()
                .map(faq -> FaqListResponse.builder()
                        .category(faq.getCategory())
                        .question(faq.getQuestion())
                        .answer(faq.getAnswer())
                        .build())
                .toList();
        
        Long totalCount = faqRepository.faqTotalCount();
        
        return PageResponse.<FaqListResponse>builder()
                .listPageResponse(faqListResponses)
                .totalCount(totalCount)
                .size(faqListResponses.size())
                .build();

    }

    
}
