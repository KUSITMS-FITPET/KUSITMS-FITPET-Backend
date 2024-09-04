package fitpet_be.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HistoryExportInfoDto {

    private String ip;
    private String refeere;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private String petInfo;
    private String petName;
    private Long petAge;
    private String petSpecies;
    private String moreInfo;
    private String phoneNumber;

    @Builder
    public HistoryExportInfoDto(String ip, String refeere,
                                LocalDateTime createdAt, String petInfo,
                                String petName, Long petAge,
                                String petSpecies, String moreInfo,
                                String phoneNumber) {

        this.ip = ip;
        this.refeere = refeere;
        this.createdAt = createdAt;
        this.petInfo = petInfo;
        this.petName = petName;
        this.petAge = petAge;
        this.petSpecies = petSpecies;
        this.moreInfo = moreInfo;
        this.phoneNumber = phoneNumber;

    }

}
