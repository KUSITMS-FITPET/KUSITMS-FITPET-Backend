package fitpet_be.application.dto.request;

import java.io.File;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardnewsUpdateRequest {

    private String title;
    private String content;
    private String imageUrl;
    private String contentDetails;

}
