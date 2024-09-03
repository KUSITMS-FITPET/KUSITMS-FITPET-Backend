package fitpet_be.presentation.controller;

import ch.qos.logback.core.subst.Token;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.service.AdminService;
import fitpet_be.common.ApiResponse;
import fitpet_be.domain.model.Admin;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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


}
