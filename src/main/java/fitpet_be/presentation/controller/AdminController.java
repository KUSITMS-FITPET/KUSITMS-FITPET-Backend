package fitpet_be.presentation.controller;

import fitpet_be.application.dto.request.AdminAccessRequest;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.dto.response.AdminDetailResponse;
import fitpet_be.application.dto.request.EstimateHistoryExportRequest;
import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.request.EstimateUpdateRequest;
import fitpet_be.application.dto.response.EstimateListResponse;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.AdminService;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.ErrorStatus;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Admin;
import fitpet_be.domain.model.Contact;
import fitpet_be.domain.model.Estimate;
import fitpet_be.domain.repository.EstimateRepository;
import fitpet_be.infrastructure.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final S3Service s3Service;
    private final EstimateRepository estimateRepository;

    @Operation(summary = "Admin 로그인", description = "Admin 계정으로 로그인 합니다")
    @PostMapping("/login")
    public ApiResponse<AdminDetailResponse> adminLogin(@RequestBody AdminLoginRequest adminLoginRequest) {

        Admin admin = adminService.AdminLogin(adminLoginRequest);

        String token = adminService.generateATAndRT(admin);

        AdminDetailResponse adminDetailResponse = AdminDetailResponse.builder()
            .accessToken(token)
            .name(admin.getName())
            .roleContents(admin.getRoleContents())
            .roleEstimates(admin.getRoleEstimates())
            .roleSites(admin.getRoleSites())
            .roleMaster(admin.getRoleMaster())
            .build();

        return ApiResponse.onSuccess(adminDetailResponse);
    }

    @Operation(summary = "Admin 생성", description = "새로운 Admin을 생성합니다")
    @PostMapping("/master/register")
    public ApiResponse<String> createAdmins(@RequestBody AdminCreateRequest adminCreateRequest, HttpServletRequest request) {

        return ApiResponse.onSuccess(adminService.createNewAdmin(adminCreateRequest));

    }

    @Operation(summary = "Admin 삭제", description = "기존 Admin을 삭제합니다")
    @Parameter(name = "adminId", description = "관리자 ID", required = true, example = "admin1")
    @DeleteMapping("/master/{adminId}")
    public ApiResponse<String> deleteAdmins(@PathVariable String adminId, HttpServletRequest request) {

        return ApiResponse.onSuccess(adminService.deleteExistAdmin(adminId));

    }

    @Operation(summary = "Admin 조회", description = "기존 Admin을 조회합니다")
    @GetMapping("/master")
    public ApiResponse<List<Admin>> getAdmins(HttpServletRequest request) {

        return ApiResponse.onSuccess(adminService.getAdminList());

    }

    @Operation(summary = "Admin 권한등록", description = "기존 Admin의 권한을 수정합니다")
    @PatchMapping("/master/access")
    public ApiResponse<String> authorizeAdmins(@RequestBody AdminAccessRequest adminAccessRequest, HttpServletRequest request) {

        return ApiResponse.onSuccess(adminService.authorizeAdmin(adminAccessRequest));

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

    @Operation(summary = "Admin 견적서 수정", description = "견적서를 주어진 정보에 따라 수정합니다.")
    @Parameter(name = "estimateId", description = "견적서 ID", required = true, example = "1")
    @PatchMapping("/estimates/{estimateId}")
    public ApiResponse<String> updateEstimateAtAdmin(
            @PathVariable("estimateId") Long estimateId,
            @RequestBody EstimateUpdateRequest request) {

        estimateService.updateEstimateAtAdmin(estimateId, request);

        return ApiResponse.onSuccess("견적서 수정본이 성공적으로 업로드되었습니다.");

    }

    @Operation(summary = "Admin History 추출(내보내기)", description = "History를 추출 후 다운로드합니다.")
    @PostMapping("/estimates/export")
    public ResponseEntity<Resource> exportHistory(@RequestBody EstimateHistoryExportRequest request)
            throws IOException {

        File file = s3Service.downloadFileFromS3("excels/OriginalSCExportFile.xlsx");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SCEstimateHistory.xlsx\"")
                .body(estimateService.exportHistory(file, request.getExportInfoDtoList()));

    }

    @Operation(summary = "Admin 견적서 다운받기", description = "견적서를 pdf 파일로 다운로드합니다.")
    @Parameter(name = "estimateId", description = "견적서 ID", required = true, example = "1")
    @PostMapping("/estimates/convert/{estimateId}")
    public ResponseEntity<String> convertExcelToPdf(@PathVariable("estimateId") Long estimateId) throws IOException {
        try {
            Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND));

            String petInfo = estimate.getPetInfo();

            return new ResponseEntity<>("PDF 파일이 성공적으로 생성되었습니다: " + estimateService.convertExcelToPdf(estimateId, petInfo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("파일 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
