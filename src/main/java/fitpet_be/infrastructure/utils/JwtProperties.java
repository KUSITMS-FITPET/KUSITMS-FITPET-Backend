package fitpet_be.infrastructure.utils;

public interface JwtProperties {
    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_HEADER_STRING = "Authorization";
    String REFRESH_HEADER_STRING = "RefreshToken";
    Boolean ROLE_CONTENTS = Boolean.TRUE;
    Boolean ROLE_ESTIMATES = true;
    Boolean ROLE_SITES = true;
    String ADMIN_ID = "id";

}
