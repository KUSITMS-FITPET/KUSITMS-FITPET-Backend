package fitpet_be.application.serviceImpl;
import fitpet_be.application.dto.EstimateUploadDto;
import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.dto.response.EstimateListResponse;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ErrorStatus;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Cardnews;
import fitpet_be.domain.model.Estimate;
import fitpet_be.domain.repository.EstimateRepository;
import fitpet_be.infrastructure.s3.S3Service;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EstimateServiceImpl implements EstimateService {

    private static final String EXCEL_FOLDER = "excels/";
    private static final String ORIGINAL_FILE_KEY = EXCEL_FOLDER + "OriginalSCFile.xlsx";

    private final EstimateRepository estimateRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void createEstimateService(EstimateServiceRequest estimateServiceRequest) throws IOException {
        Estimate saveEstimate = saveEstimate(estimateServiceRequest);
        uploadEstimate(saveEstimate);
    }

    @Override
    public void downloadEstimate(Long estimateId) {
        Estimate estimate = estimateRepository.findById(estimateId).orElseThrow(
            () -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND)
        );

        String fileUrl = estimate.getUrl();
        File tempFile = null;

        try {
            URL url = new URL(fileUrl);

            tempFile = File.createTempFile("estimate-", ".xlsx");

            try (InputStream in = url.openStream();
                FileOutputStream out = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }


                // 견적서 추출 로직

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(ErrorStatus._FILE_DOWNLOAD_FAILED);
        } finally {
            // 작업 완료 후 파일 삭제
            if (tempFile != null && tempFile.exists()) {
                if (tempFile.delete()) {
                    System.out.println("Temporary file deleted successfully.");
                } else {
                    System.err.println("Failed to delete the temporary file.");
                }
            }
        }
    }

    @Transactional
    public void uploadEstimate(Estimate estimate) throws IOException {
        // 1. S3에서 원본 파일 다운로드
        File originalFile = s3Service.downloadFileFromS3(ORIGINAL_FILE_KEY);

        // 2. 다운로드한 파일 복사 및 수정
        File modifiedFile = File.createTempFile("newFile", ".xlsx");
        try (InputStream inputStream = new FileInputStream(originalFile);
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
             FileOutputStream fos = new FileOutputStream(modifiedFile)) {

            setSheet(workbook, 0, estimate);
            setSheet(workbook, 1, estimate);
            setSheet(workbook, 2, estimate);

            workbook.write(fos);
        }

        // 3. 수정된 파일을 S3에 업로드
        EstimateUploadDto estimateUploadDto = EstimateUploadDto.builder()
                .phoneNumber(estimate.getPhoneNumber())
                .estimateId(estimate.getId())
                .createdAt(estimate.getCreatedAt())
                .file(modifiedFile)
                .build();

        String fileUrl = s3Service.uploadEstimate(estimateUploadDto);
        estimate.setUrl(fileUrl);
    }

    @Override
    public PageResponse<EstimateListResponse> getEstimateListDesc(Pageable pageable) {

        Page<Estimate> estimates = estimateRepository.findAllByOrderByDesc(pageable);

        return getEstimateListResponsePageResponse(estimates);

    }

    @Override
    public PageResponse<EstimateListResponse> getEstimateListAsc(Pageable pageable) {

        Page<Estimate> estimates = estimateRepository.findAllByOrderByAsc(pageable);

        return getEstimateListResponsePageResponse(estimates);

    }

    @Override
    public PageResponse<EstimateListResponse> getEstimateListSearch(EstimateSearchRequest request, Pageable pageable) {

        LocalDateTime startDate = LocalDateTime.parse(request.getStartDate());
        LocalDateTime endDate = LocalDateTime.parse(request.getEndDate());

        Page<Estimate> estimates =
                estimateRepository.findAllBySearch(
                        startDate, endDate,
                        request.getRefeere(), request.getPetInfo(),
                        request.getPhoneNumber(), pageable);

        return getEstimateListResponsePageResponse(estimates);

    }

    private PageResponse<EstimateListResponse> getEstimateListResponsePageResponse(Page<Estimate> estimateList) {

        List<EstimateListResponse> estimateListResponses = estimateList.stream()
                .map(estimates -> EstimateListResponse.builder()
                        .estimateId(estimates.getId())
                        .estimateIP(estimates.getIp())
                        .estimateRefeere(estimates.getRefeere())
                        .createdAt(estimates.getCreatedAt())
                        .petInfo(estimates.getPetInfo())
                        .petName(estimates.getPetName())
                        .petSpecies(estimates.getPetSpecies())
                        .moreInfo(estimates.getMoreInfo())
                        .phoneNumber(estimates.getPhoneNumber())
                        .build())
                .toList();

        Long totalCount = estimateRepository.estimateTotalCount();

        return PageResponse.<EstimateListResponse>builder()
                .listPageResponse(estimateListResponses)
                .totalCount(totalCount)
                .size(estimateListResponses.size())
                .build();

    }

    private void setSheet(XSSFWorkbook workbook, int setSheet, Estimate estimate) {
        XSSFSheet sheet = workbook.getSheetAt(setSheet);
        setValue(sheet, "C8", String.valueOf(estimate.getPetInfo()));
        setValue(sheet, "C9", String.valueOf(estimate.getPetSpecies()));
        setValue(sheet, "C11", String.valueOf(estimate.getPetAge()));
    }

    private void setValue(XSSFSheet sheet, String position, String value) {
        CellReference ref = new CellReference(position);
        Row r = sheet.getRow(ref.getRow());
        if (r != null) {
            Cell c = r.getCell(ref.getCol());
            if (c == null) {
                c = r.createCell(ref.getCol());
            }
            c.setCellValue(value);
        }
    }

    private Estimate saveEstimate(EstimateServiceRequest estimateServiceRequest) {
        Estimate estimate = estimateServiceRequest.toEntity(estimateServiceRequest);
        return estimateRepository.save(estimate);
    }
}