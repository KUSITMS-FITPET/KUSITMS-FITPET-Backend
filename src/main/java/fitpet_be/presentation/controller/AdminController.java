package fitpet_be.presentation.controller;

import fitpet_be.application.dto.request.AdminAccessRequest;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.dto.request.CardnewsCreateRequest;
import fitpet_be.application.dto.request.CardnewsDeleteRequest;
import fitpet_be.application.dto.request.CardnewsSearchRequest;
import fitpet_be.application.dto.request.CardnewsUpdateRequest;
import fitpet_be.application.dto.response.AdminDetailResponse;
import fitpet_be.application.dto.request.EstimateHistoryExportRequest;
import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.request.EstimateUpdateRequest;
import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.dto.response.EstimateListResponse;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.AdminService;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.ErrorStatus;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Admin;
import fitpet_be.domain.model.Estimate;
import fitpet_be.domain.repository.EstimateRepository;
import fitpet_be.infrastructure.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
                .body(estimateService.exportHistory(file, request.getIds()));

    }

    @Operation(summary = "Admin 견적서 다운받기", description = "견적서를 pdf 파일로 다운로드합니다.")
    @Parameter(name = "estimateId", description = "견적서 ID", required = true, example = "1")
    @PostMapping("/estimates/convert/{estimateId}")
    public ResponseEntity<Resource> convertExcelToPdf(@PathVariable("estimateId") Long estimateId) throws IOException {
        try {
            Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND));

            String phoneNumber = estimate.getPhoneNumber();
            String petInfo = estimate.getPetInfo();

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + phoneNumber + ".pdf\"")
                .body(estimateService.convertExcelToPdf(estimateId, petInfo));
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    @PostMapping("/estimates/convert/async/{estimateId}")
    public ResponseEntity<String> startFileProcessing(@PathVariable("estimateId") Long estimateId) {
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND));

        String petInfo = estimate.getPetInfo();

        // 백그라운드에서 파일 생성 시작
        estimateService.generatePdfAsync(estimateId, petInfo);

        // 즉시 응답 반환
        return ResponseEntity.accepted().body("파일이 생성 중입니다. 완료되면 다운로드할 수 있습니다.");
    }

    // 파일 상태 확인 API
    @GetMapping("/estimates/status/{estimateId}")
    public ResponseEntity<Boolean> checkFileStatus(@PathVariable("estimateId") Long estimateId) {
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND));

        String pdfUrl = estimate.getUrl();

        // 파일이 존재하는지 여부를 확인
        if (pdfUrl == null || pdfUrl.isEmpty()) {
            return ResponseEntity.ok(false);  // 파일이 아직 준비되지 않았음
        }

        return ResponseEntity.ok(true);  // 파일이 준비되었음

    }



    @GetMapping("/estimates/download/{estimateId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable("estimateId") Long estimateId) throws IOException {
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND));

        String pdfUrl = estimate.getUrl();

        if (pdfUrl == null || pdfUrl.isEmpty()) {
            throw new ApiException(ErrorStatus._FILE_NOT_FOUND);
        }

        Resource resource = new UrlResource(pdfUrl);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new File(pdfUrl).getName() + "\"")
                .body(resource);
    }





    // 카드뉴스 이미지 업로드
    @Operation(summary = "Admin 카드뉴스 생성(이미지 업로드)", description = "카드뉴스 생성할 때 필요한 이미지를 업로드합니다.")
    @PostMapping(value = "/cardNews/create/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<String> createCardNewsImg (@RequestParam("file") MultipartFile file) {  // JSON 데이터를 받는 부분

        // adminService 호출하여 파일 및 데이터 처리
        return ApiResponse.onSuccess(adminService.uploadCardNewsImg(file));

    }

    // 카드뉴스 생성
    @Operation(summary = "Admin 카드뉴스 생성(카드뉴스 업로드)", description = "카드뉴스를 생성합니다.")
    @PostMapping("/cardNews/create")
    public ApiResponse<String> createCardNews(@RequestBody CardnewsCreateRequest request) {

        return ApiResponse.onSuccess(adminService.createCardNews(request));

    }

    // 카드뉴스 수정 이미지 업로드
    @Operation(summary = "Admin 카드뉴스 수정(이미지 업로드)", description = "카드뉴스 수정할 때 필요한 이미지를 업로드합니다.")

    @PostMapping(value = "/cardNews/update/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<String> updateCardNewsImg(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long cardNewsId) {

        return ApiResponse.onSuccess(adminService.uploadCardNewsImg(file));

    }
    // 카드뉴스 수정 (작성일시 수정일시로 변경돼야 함)
    @Operation(summary = "Admin 카드뉴스 수정(카드뉴스 업로드)", description = "카드뉴스를 수정합니다.")
    @PatchMapping("/cardNews/update/{cardNewsId}")
    public ApiResponse<String> updateCardNews(
            @PathVariable Long cardNewsId,
            @RequestBody CardnewsUpdateRequest request) {

        return ApiResponse.onSuccess(adminService.updateCardNews(request, cardNewsId));

    }

    // 카드뉴스 삭제 (리스트)
    @Operation(summary = "Admin 카드뉴스 삭제", description = "카드뉴스를 삭제합니다.")
    @DeleteMapping("/cardNews/delete")
    public ApiResponse<String> deleteCardNews(@RequestBody CardnewsDeleteRequest request) {

        return ApiResponse.onSuccess(adminService.deleteCardNews(request));

    }

    // 카드뉴스 검색 (제목 + 내용)
    @Operation(summary = "Admin 카드뉴스 검색 (제목 + 내용)", description = "keyword에 해당하는 카드뉴스를 검색합니다.")
    @PostMapping("/cardNews/search")
    public ApiResponse<PageResponse<CardnewsListResponse>> getCardnewsSearch(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "12") int size,
            @RequestBody CardnewsSearchRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return ApiResponse.onSuccess(adminService.getCardnewsSearchList(request, pageable));

    }

}
