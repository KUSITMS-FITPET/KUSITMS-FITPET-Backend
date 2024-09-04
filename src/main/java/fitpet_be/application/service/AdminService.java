package fitpet_be.application.service;

import fitpet_be.application.dto.request.AdminAccessRequest;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.domain.model.Admin;
import java.util.List;

public interface AdminService {
    Admin AdminLogin(AdminLoginRequest adminLoginRequest);

    String generateATAndRT(Admin admin);

    String createNewAdmin(AdminCreateRequest adminCreateRequest);

    String deleteExistAdmin(String adminId);

    List<Admin> getAdminList();

    String authorizeAdmin(AdminAccessRequest adminAccessRequest);
}
