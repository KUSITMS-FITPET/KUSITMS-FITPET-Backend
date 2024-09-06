package fitpet_be.application.serviceImpl;
import fitpet_be.application.dto.EstimateUploadDto;
import fitpet_be.application.dto.HistoryExportInfoDto;
import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.dto.request.EstimateUpdateRequest;
import fitpet_be.application.dto.response.EstimateListResponse;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ErrorStatus;
import fitpet_be.common.PageResponse;
import fitpet_be.domain.model.Estimate;
import fitpet_be.domain.repository.EstimateRepository;
import fitpet_be.infrastructure.s3.S3Service;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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


    @Value("${aspose.clientid}")
    private String ClientId;

    @Value("${aspose.clientkey}")
    private String ClientSecret;



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
    @Transactional
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
    public Resource exportHistory(File file, List<Long> ids) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            int rowIndex = 1;

            for (Long estimateId : ids) {
                if (estimateId == -1L) {
                    List<Estimate> allEstimates = estimateRepository.findAll();

                    for (Estimate estimate : allEstimates) {
                        Row row = sheet.getRow(rowIndex);
                        if (row == null) {
                            row = sheet.createRow(rowIndex);
                        }

                        writeEstimateToRow(row, estimate, rowIndex, formatter);
                        rowIndex++;
                    }
                } else {
                    Estimate estimate = estimateRepository.findById(estimateId)
                        .orElseThrow(() -> new ApiException(ErrorStatus._ESTIMATES_NOT_FOUND));

                    Row row = sheet.getRow(rowIndex);
                    if (row == null) {
                        row = sheet.createRow(rowIndex);
                    }

                    writeEstimateToRow(row, estimate, rowIndex, formatter);
                    rowIndex++;
                }
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

    private void writeEstimateToRow(Row row, Estimate estimate, int rowIndex, DateTimeFormatter formatter) {
        row.createCell(0).setCellValue(rowIndex - 1);
        row.createCell(1).setCellValue(estimate.getIp());
        row.createCell(2).setCellValue(estimate.getRefeere());
        row.createCell(3).setCellValue(estimate.getCreatedAt().format(formatter));
        row.createCell(4).setCellValue(estimate.getPetInfo());
        row.createCell(5).setCellValue(estimate.getPetName());
        row.createCell(6).setCellValue(estimate.getPetAge());
        row.createCell(7).setCellValue(estimate.getPetSpecies());
        row.createCell(8).setCellValue(estimate.getMoreInfo());
        row.createCell(9).setCellValue(estimate.getPhoneNumber());
    }

    @Override
    public String convertExcelToPdf(Long estimateId, String petInfo) throws IOException {
        // 1. Download the file from S3
        File excelFile = s3Service.downloadFileFromS3("estimates/" + getEstimateFileName(estimateId));

        // 2. Define file paths
        String excelFilePath = excelFile.getAbsolutePath();
        String pdfFilePath = excelFilePath.replace(".xlsx", ".pdf");

        // 3. Load the Excel file using Apache POI and filter sheets
        try (FileInputStream fis = new FileInputStream(excelFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // Determine which sheets to keep
            Set<Integer> sheetsToKeep = new HashSet<>();
            if ("강아지".equals(petInfo)) {
                sheetsToKeep.add(0); // Sheet 0
                sheetsToKeep.add(1); // Sheet 1
                sheetsToKeep.add(2); // Sheet 2
            } else if ("고양이".equals(petInfo)) {
                sheetsToKeep.add(3); // Sheet 3
                sheetsToKeep.add(4); // Sheet 4
                sheetsToKeep.add(5); // Sheet 5
            } else {
                throw new IllegalArgumentException("Invalid petInfo value");
            }

            for (int i = workbook.getNumberOfSheets() - 1; i >= 0; i--) {
                if (!sheetsToKeep.contains(i)) {
                    workbook.removeSheetAt(i);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
                workbook.write(fos);
            }

            try {
                String token = getToken();
                uploadFileAndConvertToPdf(excelFilePath, pdfFilePath, token);
            } catch (Exception e) {
                throw new IOException("Failed to convert Excel to PDF", e);
            }

            // Upload the PDF file to S3
            File pdfFile = new File(pdfFilePath);
            String s3Folder = "estimatespdf/"; // Define your S3 folder path
            String pdfFileName = pdfFile.getName();
            s3Service.uploadToS3(pdfFile, s3Folder, pdfFileName);

            // Return the URL or path of the uploaded PDF file
            return s3Service.uploadToS3(pdfFile, s3Folder, pdfFileName);
        }
    }

    private void uploadFileAndConvertToPdf(String excelFilePath, String pdfFilePath, String token) throws IOException {
        File file = new File(excelFilePath);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut("https://api.aspose.cloud/v3.0/cells/convert?format=pdf");

            // Add headers
            httpPut.addHeader("Accept", "application/pdf"); // 결과를 PDF로 받기를 원하면 PDF로
            httpPut.addHeader("x-aspose-client", "Containerize.Swagger");
            httpPut.addHeader("Authorization", "Bearer " + token); // Bearer 뒤에 공백 필요

            // Add the file to the request
            HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("File", file, ContentType.DEFAULT_BINARY, file.getName()) // 파일 업로드
                .build();
            httpPut.setEntity(entity);

            // Execute the request
            try (CloseableHttpResponse response = client.execute(httpPut)) {
                HttpEntity responseEntity = response.getEntity();
                if (response.getStatusLine().getStatusCode() == 200) {
                    // Save the PDF response to a file
                    byte[] responseBytes = EntityUtils.toByteArray(responseEntity);
                    try (FileOutputStream fos = new FileOutputStream(pdfFilePath)) {
                        fos.write(responseBytes);
                    }
                } else {
                    throw new IOException("Failed to convert Excel to PDF: " + response);
                }
            }
        }
    }

    private String getToken() throws IOException {
                String clientId = ClientId;
                String clientSecret = ClientSecret;

                // 요청 URL
                String url = "https://api.aspose.cloud/connect/token";

                // 요청 본문 구성
                JSONObject json = new JSONObject();
                json.put("grant_type", "client_credentials");
                json.put("client_id", clientId);
                json.put("client_secret", clientSecret);

                // HttpClient 생성
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    // POST 요청 설정
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    httpPost.setHeader("Accept", "application/json");

                    // 본문 데이터 추가
                    StringEntity entity = new StringEntity(
                        "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret,
                        ContentType.APPLICATION_FORM_URLENCODED
                    );
                    httpPost.setEntity(entity);

                    // 요청 실행
                    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                        HttpEntity responseEntity = response.getEntity();
                        String result = EntityUtils.toString(responseEntity);

                        // 응답을 JSON으로 변환하여 토큰 추출
                        JSONObject jsonResponse = new JSONObject(result);
                        String accessToken = jsonResponse.getString("access_token");

                        return accessToken; // 토큰 반환
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
                        .petAge(estimates.getPetAge())
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
        setValue(sheet, "C8", String.valueOf(estimate.getPetName()));
        setValue(sheet, "C9", String.valueOf(estimate.getPetSpecies()));
        setValue(sheet, "C11", String.valueOf(estimate.getPetAge()));

        sheet.setForceFormulaRecalculation(true);
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