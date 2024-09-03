package fitpet_be.application.service;

import fitpet_be.application.dto.HistoryExportInfoDto;
import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.dto.request.EstimateUpdateRequest;
import fitpet_be.application.dto.response.EstimateListResponse;
import fitpet_be.common.PageResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;


public interface EstimateService {
    void createEstimateService(EstimateServiceRequest estimateServiceRequest) throws IOException;

    void downloadEstimate(Long estimateId);

    PageResponse<EstimateListResponse> getEstimateListDesc(Pageable pageable);

    PageResponse<EstimateListResponse> getEstimateListAsc(Pageable pageable);

    PageResponse<EstimateListResponse> getEstimateListSearch(EstimateSearchRequest request, Pageable pageable);

    void updateEstimateAtAdmin(Long estimateId, EstimateUpdateRequest request);

    Resource exportHistory(File file, List<HistoryExportInfoDto> exportInfos);

}