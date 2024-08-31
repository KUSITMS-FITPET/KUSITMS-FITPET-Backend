package fitpet_be.application.service;

import fitpet_be.application.dto.request.EstimateServiceRequest;
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

public interface EstimateService {
    String FILE_PATH = "/Users/jungheechan/Desktop/estimate.xlsx";

    void createEstimateService(EstimateServiceRequest estimateServiceRequest) throws IOException;
}