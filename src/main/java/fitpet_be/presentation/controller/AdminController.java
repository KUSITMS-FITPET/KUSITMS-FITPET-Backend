package fitpet_be.presentation.controller;

import ch.qos.logback.core.subst.Token;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.response.EstimateListResponse;
import fitpet_be.application.service.AdminService;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Admin;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fitpetAdmin")
public class AdminController {

    private final AdminService adminService;
    private final EstimateService estimateService;

    @Operation(summary = "Admin 로그인", description = "Admin 계정으로 로그인 합니다")
    @PostMapping("/login")
    public ApiResponse<String> AdminLogin(@RequestBody AdminLoginRequest adminLoginRequest) {

        Admin admin = adminService.AdminLogin(adminLoginRequest);

        String token = adminService.generateATAndRT(admin);

        return ApiResponse.onSuccess(token);

    }

    @GetMapping("/estimates/desc")
    public ApiResponse<PageResponse<EstimateListResponse>> getEstimateListDesc(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(estimateService.getEstimateListDesc(pageable));

    }

    @GetMapping("/estimates/desc")
    public ApiResponse<PageResponse<EstimateListResponse>> getEstimateListAsc(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(estimateService.getEstimateListAsc(pageable));

    }

    @PostMapping("/estimates/search")
    public ApiResponse<PageResponse<EstimateListResponse>> getEstimateListSearch(
            @RequestBody EstimateSearchRequest request,
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(estimateService.getEstimateListSearch(request, pageable));

    }

}
