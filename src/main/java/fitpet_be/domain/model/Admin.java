package fitpet_be.domain.model;

import fitpet_be.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {

    @Id
    @Column(name = "admin_id")
    private String id;

    @Column(name = "admin_pw", nullable = false)
    private String password;

    @Column(name = "admin_name", nullable = false)
    private String name;

    @Column(name = "admin_role_contents", nullable = false)
    private Boolean roleContents;

    @Column(name = "admin_role_estimates", nullable = false)
    private Boolean roleEstimates;

    @Column(name = "admin_role_sites", nullable = false)
    private Boolean roleSites;

    @Column(name = "admin_role_master", nullable = false)
    private Boolean roleMaster;

    @Builder
    public Admin(String id, String password,
        String name, Boolean roleContents,
        Boolean roleEstimates, Boolean roleSites, Boolean roleMaster) {

        this.id = id;
        this.password = password;
        this.name = name;
        this.roleContents = roleContents;
        this.roleEstimates = roleEstimates;
        this.roleSites = roleSites;
        this.roleMaster = roleMaster;
    }


}
