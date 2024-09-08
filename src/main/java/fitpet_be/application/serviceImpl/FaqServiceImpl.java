package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.FaqListSearchRequest;
import fitpet_be.application.dto.response.FaqCategoryResponse;
import fitpet_be.application.dto.response.FaqFaqsResponse;
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

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    public FaqListResponse getFaqList() {
        List<Faq> faqsFromDb = faqRepository.findAll();

        List<FaqCategoryResponse> categoryResponses = new ArrayList<>();
        List<FaqFaqsResponse> faqResponses = new ArrayList<>();

        Set<Long> processedCategories = new HashSet<>();

        for (Faq faq : faqsFromDb) {
            FaqFaqsResponse faqResponse = FaqFaqsResponse.builder()
                    .categoryId(faq.getCategoryId())
                    .question(faq.getQuestion())
                    .answer(faq.getAnswer())
                    .build();
            faqResponses.add(faqResponse);

            if (!processedCategories.contains(faq.getCategoryId())) {
                FaqCategoryResponse categoryResponse = FaqCategoryResponse.builder()
                        .id(faq.getCategoryId())
                        .name(faq.getCategoryName())
                        .build();
                categoryResponses.add(categoryResponse);
                processedCategories.add(faq.getCategoryId());
            }
        }

        return FaqListResponse.builder()
                .categories(categoryResponses)
                .faqs(faqResponses)
                .build();
    }

    
}
