package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.AdminAccessRequest;
import fitpet_be.application.dto.request.AdminCreateRequest;
import fitpet_be.application.dto.request.AdminLoginRequest;
import fitpet_be.application.dto.request.CardnewsCreateRequest;
import fitpet_be.application.dto.request.CardnewsDeleteRequest;
import fitpet_be.application.dto.request.CardnewsSearchRequest;
import fitpet_be.application.dto.request.CardnewsUpdateRequest;
import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.AdminService;
import fitpet_be.common.ErrorStatus;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Admin;
import fitpet_be.domain.model.Cardnews;
import fitpet_be.domain.repository.AdminRepository;
import fitpet_be.domain.repository.CardnewsRepository;
import fitpet_be.infrastructure.jwt.JwtProvider;
import fitpet_be.infrastructure.s3.S3Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final CardnewsRepository cardnewsRepository;

    private final S3Service s3Service;

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
    @Transactional
    public String uploadCardNewsImg(MultipartFile multipartFile) {

        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileUrl = s3Service.uploadCardnews(file);

            return fileUrl;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional
    public String createCardNews(CardnewsCreateRequest request) {

        cardnewsRepository.save(request.toEntity());

        return "카드뉴스 생성 성공!";

    }

    @Override
    @Transactional
    public String updateCardNews(CardnewsUpdateRequest request, Long cardNewsId) {

        Cardnews cardnews = cardnewsRepository.findById(cardNewsId)
                .orElseThrow(() -> new ApiException(ErrorStatus._CARDNEWS_NOT_FOUND));


        cardnews.updateCardnews(request.getTitle(), request.getContent(), request.getImageUrl(), request.getContentDetails());

        cardnewsRepository.save(cardnews);

        return "카드뉴스 업데이트 성공!";

    }

    @Override
    @Transactional
    public String deleteCardNews(CardnewsDeleteRequest request) {

        List<Cardnews> cardNewsList = cardnewsRepository.findAllById(request.getCardNewsIds());

        if (cardNewsList.isEmpty()) {
            throw new ApiException(ErrorStatus._CARDNEWS_NOT_FOUND);
        }

        cardnewsRepository.deleteAllById(request.getCardNewsIds());

        return "카드뉴스 삭제 성공했습니다.";

    }

    @Override
    public PageResponse<CardnewsListResponse> getCardnewsSearchList(CardnewsSearchRequest request, Pageable pageable) {

        Page<Cardnews> cardNewsPage = cardnewsRepository.searchAllByKeyword(request.getKeyword(), pageable);

        List<CardnewsListResponse> cardNewsListResponses = cardNewsPage.stream()
                .map(cardnews -> CardnewsListResponse.builder()
                        .cardNewsTitle(cardnews.getTitle())
                        .cardNewsContent(cardnews.getContent())
                        .image_url(cardnews.getImageUrl())
                        .build())
                .toList();

        Long totalCount = cardnewsRepository.cardNewsTotalCount();

        return PageResponse.<CardnewsListResponse>builder()
                .listPageResponse(cardNewsListResponses)
                .totalCount(totalCount)
                .size(cardNewsListResponses.size())
                .build();

    }


    private void saveAdmin(AdminCreateRequest adminCreateRequest) {
        Admin admin = adminCreateRequest.toEntity(adminCreateRequest);
        adminRepository.save(admin);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    // S3 URL에서 객체 키를 추출
    private String cardNewsExtractS3ObjectKey(String s3Url) {
        // URL에서 버킷 경로 이후의 키 부분을 추출 (예: cardnews/image.jpg)
        return s3Url.substring(s3Url.indexOf("cardnews"));
    }

}
