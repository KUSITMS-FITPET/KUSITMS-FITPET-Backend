package fitpet_be.presentation.controller;

import ch.qos.logback.core.subst.Token;
import fitpet_be.application.dto.request.AdminAccessRequest;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.dto.response.AdminDetailResponse;
import fitpet_be.application.service.AdminService;
import fitpet_be.common.ApiResponse;
import fitpet_be.domain.model.Admin;
import fitpet_be.domain.model.Contact;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fitpetAdmin")
public class AdminController {

    private final AdminService adminService;

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


}
