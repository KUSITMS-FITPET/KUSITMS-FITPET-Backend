package fitpet_be.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdminAccessRequest {
    private String adminId;
    private Boolean roleContents;
    private Boolean roleEstimates;
    private Boolean roleSites;
    private Boolean roleMaster;

    @Builder
    public AdminAccessRequest(String adminId, Boolean roleContents
        , Boolean roleEstimates, Boolean roleSites, Boolean roleMaster) {

        this.adminId = adminId;
        this.roleContents = roleContents;
        this.roleEstimates = roleEstimates;
        this.roleSites = roleSites;
        this.roleMaster = roleMaster;

    }
}
