package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.AdminAccessRequest;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.AdminService;
import fitpet_be.common.ErrorStatus;
import fitpet_be.domain.model.Admin;
import fitpet_be.domain.repository.AdminRepository;
import fitpet_be.infrastructure.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;

    @Override
    public Admin AdminLogin(AdminLoginRequest adminLoginRequest) {
        Admin admin = adminRepository.findById(adminLoginRequest.getAdminId()).orElseThrow(
            () -> new ApiException(ErrorStatus._ADMIN_NOT_FOUND));

        if (Objects.equals(admin.getPassword(), adminLoginRequest.getAdminPw())) {
            return admin;
        }
        else {
            throw new ApiException(ErrorStatus._ADMIN_NOT_VALID);
        }

    }

    @Override
    public String generateATAndRT(Admin admin) {

        return jwtProvider.generateAccessToken(admin.getId(), admin.getRoleContents(), admin.getRoleEstimates(), admin.getRoleSites(), admin.getRoleMaster());
    }

    @Transactional
    public String createNewAdmin(AdminCreateRequest adminCreateRequest) {
        saveAdmin(adminCreateRequest);
        return "관리자가 등록 되었습니다";
    }

    @Transactional
    public String deleteExistAdmin(String adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
            () -> new ApiException(ErrorStatus._ADMIN_NOT_FOUND)
        );

        adminRepository.delete(admin);

        return "관리자가 삭제 되었습니다";
    }

    @Override
    public List<Admin> getAdminList() {
        return adminRepository.findAllByOrderByDesc();
    }

    @Transactional
    public String authorizeAdmin(AdminAccessRequest adminAccessRequest) {
        String adminId = adminAccessRequest.getAdminId();
        Admin admin = adminRepository.findById(adminId).orElseThrow(
            () -> new ApiException(ErrorStatus._ADMIN_NOT_FOUND)
        );

        admin.setRole(adminAccessRequest.getRoleContents(), adminAccessRequest.getRoleEstimates(),
            adminAccessRequest.getRoleSites(), adminAccessRequest.getRoleMaster());

        return "관리자 권한이 등록되었습니다";
    }

    @Override
    public HttpServletResponse addCookies(Admin admin, HttpServletResponse response) {

        String estimateRole = admin.getRoleEstimates().toString();
        String siteRole = admin.getRoleSites().toString();
        String masterRole = admin.getRoleMaster().toString();
        String contentsRole = admin.getRoleContents().toString();

        // 각각의 쿠키 생성 및 설정
        Cookie estimateRoleCookie = new Cookie("estimate_role", estimateRole);
        estimateRoleCookie.setHttpOnly(true);
        estimateRoleCookie.setPath("/");
        estimateRoleCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(estimateRoleCookie);

        Cookie siteRoleCookie = new Cookie("site_role", siteRole);
        siteRoleCookie.setHttpOnly(true);
        siteRoleCookie.setPath("/");
        siteRoleCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(siteRoleCookie);

        Cookie masterRoleCookie = new Cookie("master_role", masterRole);
        masterRoleCookie.setHttpOnly(true);
        masterRoleCookie.setPath("/");
        masterRoleCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(masterRoleCookie);

        Cookie contentsRoleCookie = new Cookie("contents_role", contentsRole);
        contentsRoleCookie.setHttpOnly(true);
        contentsRoleCookie.setPath("/");
        contentsRoleCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(contentsRoleCookie);

        return response;
    }

    private void saveAdmin(AdminCreateRequest adminCreateRequest) {
        Admin admin = adminCreateRequest.toEntity(adminCreateRequest);
        adminRepository.save(admin);
    }
}
