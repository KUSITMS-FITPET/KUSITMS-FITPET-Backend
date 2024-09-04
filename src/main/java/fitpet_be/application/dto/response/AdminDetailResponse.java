package fitpet_be.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminDetailResponse {
    private String accessToken;
    private String name;
    private Boolean roleContents;
    private Boolean roleEstimates;
    private Boolean roleSites;
    private Boolean roleMaster;
    @Builder
    public AdminDetailResponse(
        String accessToken, String name,
        Boolean roleContents, Boolean roleEstimates, Boolean roleSites, Boolean roleMaster) {

        this.accessToken = accessToken;
        this.name = name;
        this.roleContents = roleContents;
        this.roleEstimates = roleEstimates;
        this.roleSites = roleSites;
        this.roleMaster = roleMaster;

    }
}
