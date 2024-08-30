package fitpet_be.application.service;

import fitpet_be.application.request.EstimateRequest;
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
public class EstimateService {

    private final EstimateRepository estimateRepository;
    public static final String FILE_PATH = "/Users/jungheechan/Desktop/estimate.xlsx";

    @Transactional
    public void createEstimateService(EstimateRequest estimateRequest) throws IOException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            throw new IOException("File does not exist");
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            setValue(sheet, "C8", String.valueOf(estimateRequest.getPetInfo()));
            setValue(sheet, "C9", String.valueOf(estimateRequest.getPetSpecies()));
            setValue(sheet, "C11", String.valueOf(estimateRequest.getPetAge()));

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        saveEstimate(estimateRequest);
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

    private void saveEstimate(EstimateRequest estimateRequest) {
        Estimate estimate = estimateRequest.toEntity(estimateRequest);
        estimateRepository.save(estimate);
    }

}
