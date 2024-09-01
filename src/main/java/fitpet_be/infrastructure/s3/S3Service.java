package fitpet_be.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import fitpet_be.application.exception.ApiException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
@Slf4j
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    private static final String ESTIMATES_FOLDER = "estimates/";
    private static final String EXCEL_FOLDER = "excels/";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 파일 확장자 가져오기
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    // 고유한 파일명 생성
    // TODO: 파일명 생성 규칙 정할 것
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 엑셀 업로드
    public String uploadExcel(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        return uploadToS3(file, EXCEL_FOLDER, fileName);
    }

    // 견적서 업로드
    public String uploadEstimate(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        return uploadToS3(file, ESTIMATES_FOLDER, fileName);
    }

    // S3에 업로드 공통 로직
    private String uploadToS3(MultipartFile file, String folder, String fileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, folder + fileName, inputStream,
                objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "파일 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, folder + fileName).toString();
    }


}
