package fitpet_be.presentation.controller;

import fitpet_be.application.dto.request.UrlCreateRequest;
import fitpet_be.application.dto.response.UrlListResponse;
import fitpet_be.application.service.UrlService;
import fitpet_be.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;
    @PostMapping
    public ApiResponse<String> createUrls(@RequestBody UrlCreateRequest urlCreateRequest, HttpServletRequest request) {

        return ApiResponse.onSuccess(urlService.createUrls(urlCreateRequest));

    }

    @GetMapping
    public ApiResponse<List<UrlListResponse>> getUrls(HttpServletRequest request) {

        return ApiResponse.onSuccess(urlService.getUrlList());

    }




}
