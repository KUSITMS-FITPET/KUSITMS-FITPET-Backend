package fitpet_be.application.dto.request;

import fitpet_be.domain.model.Admin;
import fitpet_be.domain.model.Estimate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdminCreateRequest {
    private String adminId;
    private String adminPw;
    private String name;
    private Boolean roleContents;
    private Boolean roleEstimates;
    private Boolean roleSites;
    private Boolean roleMaster;

    @Builder
    public AdminCreateRequest(String adminId, String adminPw, String name, Boolean roleContents
    , Boolean roleEstimates, Boolean roleSites, Boolean roleMaster) {

        this.adminId = adminId;
        this.adminPw = adminPw;
        this.name = name;
        this.roleContents = roleContents;
        this.roleEstimates = roleEstimates;
        this.roleSites = roleSites;
        this.roleMaster = roleMaster;

    }

    public Admin toEntity(AdminCreateRequest adminCreateRequest) {
        return Admin.builder()
            .id(adminCreateRequest.getAdminId())
            .password(adminCreateRequest.getAdminPw())
            .name(adminCreateRequest.getName())
            .roleContents(adminCreateRequest.getRoleContents())
            .roleEstimates(adminCreateRequest.getRoleEstimates())
            .roleSites(adminCreateRequest.getRoleSites())
            .roleMaster(adminCreateRequest.getRoleMaster())
            .build();
    }
}
