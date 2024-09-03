package fitpet_be.presentation.controller;

import ch.qos.logback.core.subst.Token;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.request.EstimateUpdateRequest;
import fitpet_be.application.dto.response.EstimateListResponse;
import fitpet_be.application.service.AdminService;
import fitpet_be.application.service.EstimateService;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Admin;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ApiResponse<String> adminLogin(@RequestBody AdminLoginRequest adminLoginRequest) {

        Admin admin = adminService.AdminLogin(adminLoginRequest);

        String token = adminService.generateATAndRT(admin);

        return ApiResponse.onSuccess(token);

    }

    @Operation(summary = "Admin 생성", description = "새로운 Admin을 생성합니다")
    @PostMapping("/register")
    public ApiResponse<String> createAdmin(@RequestBody AdminCreateRequest adminCreateRequest, HttpServletRequest request) {

        return ApiResponse.onSuccess(adminService.createNewAdmin(adminCreateRequest));

    }



    @Operation(summary = "Admin 견적서 불러오기", description = "견적서를 최신순으로 조회합니다.")
    @GetMapping("/estimates/desc")
    public ApiResponse<PageResponse<EstimateListResponse>> getEstimateListDesc(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(estimateService.getEstimateListDesc(pageable));

    }

    @Operation(summary = "Admin 견적서 불러오기", description = "견적서를 오래된순으로 조회합니다.")
    @GetMapping("/estimates/asc")
    public ApiResponse<PageResponse<EstimateListResponse>> getEstimateListAsc(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(estimateService.getEstimateListAsc(pageable));

    }

    @Operation(summary = "Admin 견적서 검색(필터링)", description = "견적서를 주어진 정보에 따라 필터링합니다.")
    @PostMapping("/estimates/search")
    public ApiResponse<PageResponse<EstimateListResponse>> getEstimateListSearch(
            @RequestBody EstimateSearchRequest request,
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(estimateService.getEstimateListSearch(request, pageable));

    }

    @PatchMapping("/estimates/{estimateId}")
    public ApiResponse<String> updateEstimateAtAdmin(
            @PathVariable("estimateId") Long estimateId,
            @RequestBody EstimateUpdateRequest request) {

        estimateService.updateEstimateAtAdmin(estimateId, request);

        return ApiResponse.onSuccess("견적서 수정본이 성공적으로 업로드되었습니다.");

    }

}
