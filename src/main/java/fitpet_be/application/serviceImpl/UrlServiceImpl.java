package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.UrlCreateRequest;
import fitpet_be.application.dto.response.UrlListResponse;
import fitpet_be.application.service.UrlService;
import fitpet_be.domain.model.Url;
import fitpet_be.domain.repository.UrlRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    @Transactional
    public String createUrls(UrlCreateRequest urlCreateRequest) {

        Url url = urlCreateRequest.toEntity(urlCreateRequest);
        urlRepository.save(url);

        return "새로운 Url이 등록되었습니다";
    }

    @Override
    public List<UrlListResponse> getUrlList() {
        List<Url> urls = urlRepository.findAll();

        return urls.stream()
            .map(url -> UrlListResponse.builder()
                .id(url.getId())
                .value(url.getValue())
                .name(url.getName())
                .build())
            .collect(Collectors.toList());
    }
}
