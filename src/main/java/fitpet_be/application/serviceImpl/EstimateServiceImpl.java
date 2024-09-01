package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.EstimateExportRequest;
import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.exception.ApiException;
import fitpet_be.application.service.EstimateService;
import fitpet_be.common.ErrorStatus;
import fitpet_be.domain.model.Estimate;
import fitpet_be.domain.repository.EstimateRepository;
import fitpet_be.infrastructure.s3.S3Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EstimateServiceImpl implements EstimateService {

    private final EstimateRepository estimateRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void createEstimateService(EstimateServiceRequest estimateServiceRequest) throws IOException {

        String fileName = "estimate.xlsx";
        File excelFile = s3Service.downloadExcel(fileName);

        if (!excelFile.exists()) {
            throw new IOException("File does not exist");
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelFile))) {

            setSheet(workbook, 0, estimateServiceRequest);
            setSheet(workbook, 1, estimateServiceRequest);
            setSheet(workbook, 2, estimateServiceRequest);

            try (FileOutputStream fileOut = new FileOutputStream(excelFile)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // 로컬 임시 파일 삭제
            if (excelFile.exists()) {
                excelFile.delete();
            }
        }

        saveEstimate(estimateServiceRequest);
    }

    @Override
    public void exportEstimateService(EstimateExportRequest estimateExportRequest)
        throws IOException {

        String fileName = "exportEstimate.xlsx";
        File excelFile = s3Service.downloadExcel(fileName);

        List<Long> ids = estimateExportRequest.getIds();

        for (Long id : ids) {
            Estimate estimate = estimateRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._ESTIMATE_NOT_FOUND)
            );
            // Estimate로 견적서에 값 입력하기

        }
    }


    private void setSheet(XSSFWorkbook workbook, int setSheet, EstimateServiceRequest estimateServiceRequest){
        XSSFSheet sheet = workbook.getSheetAt(setSheet);

        setValue(sheet, "C8", String.valueOf(estimateServiceRequest.getPetInfo()));
        setValue(sheet, "C9", String.valueOf(estimateServiceRequest.getPetSpecies()));
        setValue(sheet, "C11", String.valueOf(estimateServiceRequest.getPetAge()));

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

    private void saveEstimate(EstimateServiceRequest estimateServiceRequest) {
        Estimate estimate = estimateServiceRequest.toEntity(estimateServiceRequest);
        estimateRepository.save(estimate);
    }
}