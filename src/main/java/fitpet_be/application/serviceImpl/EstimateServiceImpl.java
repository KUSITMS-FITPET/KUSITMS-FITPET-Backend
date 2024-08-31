package fitpet_be.application.serviceImpl;

import fitpet_be.application.dto.request.EstimateServiceRequest;
import fitpet_be.application.service.EstimateService;
import fitpet_be.domain.model.Estimate;
import fitpet_be.domain.repository.EstimateRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @Override
    @Transactional
    public void createEstimateService(EstimateServiceRequest estimateServiceRequest) throws IOException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            throw new IOException("File does not exist");
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            setValue(sheet, "C8", String.valueOf(estimateServiceRequest.getPetInfo()));
            setValue(sheet, "C9", String.valueOf(estimateServiceRequest.getPetSpecies()));
            setValue(sheet, "C11", String.valueOf(estimateServiceRequest.getPetAge()));

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        saveEstimate(estimateServiceRequest);
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