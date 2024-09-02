package fitpet_be.application.dto;

import java.io.File;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Getter
@NoArgsConstructor
public class EstimateUploadDto {

    private Long estimateId;
    private String phoneNumber;
    private File file;
    private LocalDateTime createdAt;

    @Builder
    public EstimateUploadDto(Long estimateId, String phoneNumber, File file, LocalDateTime createdAt) {

        this.estimateId = estimateId;
        this.phoneNumber = phoneNumber;
        this.file = file;
        this.createdAt = createdAt;

    }

}
