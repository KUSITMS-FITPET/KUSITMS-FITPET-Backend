package fitpet_be.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdminLoginRequest {
    private String adminId;
    private String adminPw;

    @Builder
    public AdminLoginRequest(String adminId, String adminPw) {

        this.adminId = adminId;
        this.adminPw = adminPw;

    }
}
