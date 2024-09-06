package fitpet_be.application.service;

import fitpet_be.application.dto.request.AdminAccessRequest;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.dto.request.CardnewsCreateRequest;
import fitpet_be.application.dto.request.CardnewsDeleteRequest;
import fitpet_be.application.dto.request.CardnewsSearchRequest;
import fitpet_be.application.dto.request.CardnewsUpdateRequest;
import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Admin;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    Admin AdminLogin(AdminLoginRequest adminLoginRequest);

    String generateATAndRT(Admin admin);

    String createNewAdmin(AdminCreateRequest adminCreateRequest);

    String deleteExistAdmin(String adminId);

    List<Admin> getAdminList();

    String authorizeAdmin(AdminAccessRequest adminAccessRequest);

    String createCardNewsImg(MultipartFile multipartFile);

    String createCardNews(CardnewsCreateRequest request);

    String updateCardNewsImg(MultipartFile multipartFile, Long cardNewsId);

    String updateCardNews(CardnewsUpdateRequest request, Long cardNewsId);

    String deleteCardNews(CardnewsDeleteRequest request);

    PageResponse<CardnewsListResponse> getCardnewsSearchList(CardnewsSearchRequest request, Pageable pageable);

}
