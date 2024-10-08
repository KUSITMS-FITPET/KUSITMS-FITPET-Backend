package fitpet_be.infrastructure.s3;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import fitpet_be.application.dto.EstimateUploadDto;
import fitpet_be.application.dto.request.CardnewsCreateRequest;
import java.time.format.DateTimeFormatter;
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
    private static final String CARDNEWS_FOLDER = "cardnews/";
    public static final String ORIGINAL_FILE_KEY = EXCEL_FOLDER + "OriginalSCFile.xlsx";
    public static final String ORIGINAL_EXPORT_FILE_KEY = EXCEL_FOLDER + "OriginalSCExportFile.xlsx";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 고유한 파일명 생성
    private String createFileName(EstimateUploadDto estimateUploadDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String formattedDate = estimateUploadDto.getCreatedAt().format(formatter);

        return estimateUploadDto.getPhoneNumber() + "_" + formattedDate + ".xlsx";

    }

    // 견적서 업로드
    public String uploadEstimate(EstimateUploadDto estimateUploadDto) throws IOException {
        String fileName = createFileName(estimateUploadDto);
        File file = estimateUploadDto.getFile(); //

        return uploadToS3(file, ESTIMATES_FOLDER, fileName);
    }

    public String uploadCardnews(File file) throws IOException {

        return uploadToS3(file, CARDNEWS_FOLDER, file.getName());

    }



    // S3에서 파일 다운로드 후 File로 변환
    public File downloadFileFromS3(String s3Key) throws IOException {
        System.out.println("s3Key = " + s3Key);
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
    public String uploadToS3(File file, String folder, String fileName) {
        try (InputStream inputStream = new FileInputStream(file)) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.length());
            objectMetadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            amazonS3Client.putObject(new PutObjectRequest(bucket, folder + fileName, inputStream, objectMetadata));

            return amazonS3Client.getUrl(bucket, folder + fileName).toString();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }

}
