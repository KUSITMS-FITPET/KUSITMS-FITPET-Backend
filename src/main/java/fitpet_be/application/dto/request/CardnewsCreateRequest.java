package fitpet_be.application.dto.request;

import fitpet_be.domain.model.Cardnews;
import java.io.File;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class CardnewsCreateRequest {

    private String title;
    private String content;
    private String imgUrl;

    public Cardnews toEntity() {

        return Cardnews.builder()
                .title(title)
                .content(content)
                .imageUrl(imgUrl)
                .build();

    }

}
