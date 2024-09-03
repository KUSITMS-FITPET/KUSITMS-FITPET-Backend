package fitpet_be.application.service;

import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.domain.model.Admin;

public interface AdminService {
    Admin AdminLogin(AdminLoginRequest adminLoginRequest);

    String generateATAndRT(Admin admin);

    String createNewAdmin(AdminCreateRequest adminCreateRequest);
}
