package fitpet_be.application.serviceImpl;
import com.itextpdf.io.image.ImageDataFactory;
import fitpet_be.application.dto.EstimateUploadDto;
import fitpet_be.application.dto.HistoryExportInfoDto;
import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.dto.request.EstimateUpdateRequest;
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
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import com.itextpdf.layout.element.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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

    public static final String ORIGINAL_EXPORT_FILE_KEY = EXCEL_FOLDER + "OriginalSCExportFile.xlsx";

    private final EstimateRepository estimateRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void createEstimateService(EstimateServiceRequest estimateServiceRequest) throws IOException {
        Estimate saveEstimate = saveEstimate(estimateServiceRequest);
        uploadEstimate(saveEstimate);
    }

    @Override
    public String getEstimateFileName(Long estimateId) {

        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND));

        return estimate.getPhoneNumber() + "_" + estimate.getCreatedAt() + ".xlsx";

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

        // DateTimeFormatter를 사용하여 정확한 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // startDate와 endDate를 LocalDateTime으로 변환
        LocalDateTime startDate = LocalDateTime.parse(request.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(request.getEndDate(), formatter);

        Page<Estimate> estimates =
                estimateRepository.findAllBySearch(
                        startDate, endDate,
                        request.getRefeere(), request.getPetInfo(),
                        request.getPhoneNumber(), pageable);

        return getEstimateListResponsePageResponse(estimates);

    }

    @Override
    public void updateEstimateAtAdmin(Long estimateId, EstimateUpdateRequest request) {

        Estimate originalEstimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND));

        Estimate newEstimate = Estimate.builder()
                .ip(originalEstimate.getIp())
                .agreement(originalEstimate.getAgreement())
                .moreInfo(request.getMoreInfo())
                .petAge(request.getPetAge())
                .petInfo(request.getPetInfo())
                .petName(request.getPetName())
                .petSpecies(request.getPetSpecies())
                .phoneNumber(request.getPhoneNumber())
                .build();

        try {
            uploadEstimate(newEstimate);
            estimateRepository.save(newEstimate);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(ErrorStatus._ESTIMATE_UPLOAD_FAILED);
        }

    }

    // 견적서 히스토리 추출
    @Override
    public Resource exportHistory(File file, List<HistoryExportInfoDto> exportInfos) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            int rowIndex = 1;

            for (HistoryExportInfoDto info : exportInfos) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {

                    row = sheet.createRow(rowIndex);

                }

                System.out.println(info.getCreatedAt());
                row.createCell(0).setCellValue(rowIndex - 1);
                row.createCell(1).setCellValue(info.getIp());
                row.createCell(2).setCellValue(info.getRefeere());
                row.createCell(3).setCellValue(info.getCreatedAt().format(formatter));
                row.createCell(4).setCellValue(info.getPetInfo());
                row.createCell(5).setCellValue(info.getPetName());
                row.createCell(6).setCellValue(info.getPetAge());
                row.createCell(7).setCellValue(info.getPetSpecies());
                row.createCell(8).setCellValue(info.getMoreInfo());
                row.createCell(9).setCellValue(info.getPhoneNumber());
                rowIndex++;

            }

            // 파일을 저장할 위치를 시스템의 기본 임시 디렉토리로 설정
            String tempDir = System.getProperty("java.io.tmpdir");
            File outputFile = new File(tempDir + "/modified-" + file.getName());

            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                workbook.write(outputStream);
            } finally {
                workbook.close();
            }

            return new FileSystemResource(outputFile);

        } catch(Exception e) {
            System.out.println("다운로드 실패 " + e);
            throw new ApiException(ErrorStatus._FILE_DOWNLOAD_FAILED);

        }

    }

    @Override
    public void convertExcelToPdf(String excelFilePath, String pdfFilePath) {
        try {
            // 파이썬 스크립트 실행
            ProcessBuilder pb = new ProcessBuilder("python", "/app/script.py", excelFilePath, pdfFilePath);
            Process process = pb.start();

            // 파이썬 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("엑셀 파일이 PDF로 성공적으로 변환되었습니다.");
            } else {
                System.out.println("엑셀 파일 변환 중 오류 발생.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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