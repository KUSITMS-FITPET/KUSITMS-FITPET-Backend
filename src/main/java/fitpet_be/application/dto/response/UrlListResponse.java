package fitpet_be.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UrlListResponse {

    private Long id;
    private String value;
    private String name;

    @Builder
    public UrlListResponse(Long id, String value,
        String name) {
        this.id = id;
        this.value = value;
        this.name = name;
    }

}
