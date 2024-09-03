package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.AdminService;
import fitpet_be.common.ErrorStatus;
import fitpet_be.domain.model.Admin;
import fitpet_be.domain.repository.AdminRepository;
import fitpet_be.infrastructure.jwt.JwtProvider;
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

    private void saveAdmin(AdminCreateRequest adminCreateRequest) {
        Admin admin = adminCreateRequest.toEntity(adminCreateRequest);
        adminRepository.save(admin);
    }
}
