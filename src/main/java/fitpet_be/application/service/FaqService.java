package fitpet_be.application.service;

import fitpet_be.application.dto.request.FaqListSearchRequest;
import fitpet_be.application.dto.response.FaqListResponse;
import fitpet_be.common.PageResponse;
import org.springframework.data.domain.Pageable;


public interface FaqService {

    FaqListResponse getFaqList();

}
