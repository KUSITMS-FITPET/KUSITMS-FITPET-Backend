package fitpet_be.application.dto.request;

import fitpet_be.domain.model.Admin;
import fitpet_be.domain.model.Url;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UrlCreateRequest {
    private String value;
    private String name;

    @Builder
    public UrlCreateRequest(String value, String name) {

        this.value = value;
        this.name = name;

    }

    public Url toEntity(UrlCreateRequest urlCreateRequest) {
        return Url.builder()
            .value(urlCreateRequest.getValue())
            .name(urlCreateRequest.getName())
            .build();
    }
}
