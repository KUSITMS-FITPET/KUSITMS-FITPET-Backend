package fitpet_be.infrastructure.s3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import fitpet_be.application.dto.EstimateUploadDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
@Slf4j
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    private static final String ESTIMATES_FOLDER = "estimates/";
    private static final String EXCEL_FOLDER = "excels/";
    public static final String ORIGINAL_FILE_KEY = EXCEL_FOLDER + "OriginalSCFile.xlsx";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 고유한 파일명 생성
    private String createFileName(EstimateUploadDto estimateUploadDto) {
        return estimateUploadDto.getPhoneNumber() + "_" + estimateUploadDto.getCreatedAt() + ".xlsx";
    }

    // 견적서 업로드
    public void uploadEstimate(EstimateUploadDto estimateUploadDto) throws IOException {
        String fileName = createFileName(estimateUploadDto);
        File file = estimateUploadDto.getFile(); // DTO에서 File 객체를 가져옴
        uploadToS3(file, ESTIMATES_FOLDER, fileName);
    }

    // S3에서 파일 다운로드 후 File로 변환
    public File downloadFileFromS3(String s3Key) throws IOException {
        S3Object s3Object = amazonS3Client.getObject(bucket, s3Key);
        InputStream inputStream = s3Object.getObjectContent();
        File tempFile = File.createTempFile("temp", ".xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] readBuf = new byte[1024];
            int readLen;
            while ((readLen = inputStream.read(readBuf)) > 0) {
                outputStream.write(readBuf, 0, readLen);
            }
        }
        return tempFile;
    }

    // S3에 File 객체를 업로드
    private void uploadToS3(File file, String folder, String fileName) {
        try (InputStream inputStream = new FileInputStream(file)) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.length());
            objectMetadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            amazonS3Client.putObject(new PutObjectRequest(bucket, folder + fileName, inputStream, objectMetadata));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }
}
