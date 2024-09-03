package fitpet_be.application.service;

import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.dto.response.EstimateListResponse;
import fitpet_be.common.PageResponse;
import java.io.IOException;
import org.springframework.data.domain.Pageable;


public interface EstimateService {
    void createEstimateService(EstimateServiceRequest estimateServiceRequest) throws IOException;

    void downloadEstimate(Long estimateId);

    PageResponse<EstimateListResponse> getEstimateListDesc(Pageable pageable);

    PageResponse<EstimateListResponse> getEstimateListAsc(Pageable pageable);

    PageResponse<EstimateListResponse> getEstimateListSearch(EstimateSearchRequest request, Pageable pageable);

}