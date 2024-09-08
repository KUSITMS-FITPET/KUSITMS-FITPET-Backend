package fitpet_be.application.service;

import fitpet_be.application.dto.request.ReviewServiceRequest;
import fitpet_be.application.dto.request.UrlCreateRequest;
import fitpet_be.application.dto.response.UrlListResponse;
import java.util.List;

public interface UrlService {
    String createUrls(UrlCreateRequest urlCreateRequest);

    List<UrlListResponse> getUrlList();
}
