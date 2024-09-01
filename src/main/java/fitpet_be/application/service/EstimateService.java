package fitpet_be.application.service;

import fitpet_be.application.dto.request.EstimateServiceRequest;
import java.io.IOException;

public interface EstimateService {
    void createEstimateService(EstimateServiceRequest estimateServiceRequest) throws IOException;
}