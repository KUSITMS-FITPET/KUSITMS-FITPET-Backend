package fitpet_be.infrastructure.jwt;

import static fitpet_be.infrastructure.utils.JwtProperties.ADMIN_ID;
import static fitpet_be.infrastructure.utils.JwtProperties.ROLE_CONTENTS;
import static fitpet_be.infrastructure.utils.JwtProperties.ROLE_ESTIMATES;
import static fitpet_be.infrastructure.utils.JwtProperties.ROLE_SITES;

import fitpet_be.application.exception.ApiException;
import fitpet_be.common.ErrorStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final Key key;
    private final Long ACCESS_TOKEN_EXPIRE_TIME;
    private final Long REFRESH_TOKEN_EXPIRE_TIME;


    public JwtProvider(@Value("${jwt.secret_key}") String secretKey,
        @Value("${jwt.access_token_expire}") Long accessTokenExpire,
        @Value("${jwt.refresh_token_expire}") Long refreshTokenExpire) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpire;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpire;

    }

    /**
     * JWT header "alg" : "HS512" payload "id" : "employeeId" payload "auth" : "EMPLOYEE/ADMIN"
     * payload "iat" : "123456789" payload "exp" : "123456789"
     */
    public String generateAccessToken(String adminId, Boolean roleContents, Boolean roleEstimates, Boolean roleSites) {

        Date expiredAt = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
            .claim(ADMIN_ID, adminId)
            .claim("ROLE_CONTENTS", roleContents)  // boolean 값
            .claim("ROLE_ESTIMATES", roleEstimates)  // boolean 값
            .claim("ROLE_SITES", roleSites)  // boolean 값
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setExpiration(expiredAt)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(String adminId, Boolean roleContents, Boolean roleEstimates, Boolean roleSites) {

        Date expiredAt = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
            .claim(ADMIN_ID, adminId)
            .claim("ROLE_CONTENTS", roleContents)  // boolean 값
            .claim("ROLE_ESTIMATES", roleEstimates)  // boolean 값
            .claim("ROLE_SITES", roleSites)  // boolean 값
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setExpiration(expiredAt)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Long getAdminId(String token) {
        return parseClaims(token)
            .get(ADMIN_ID, Long.class);
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorStatus._JWT_INVALID);
        }
    }

    public String validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return "VALID";
        } catch (ExpiredJwtException e) {
            return "EXPIRED";
        } catch (SignatureException | MalformedJwtException e) {
            return "INVALID";
        }
    }


    private void setContextHolder(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Long getExpiration(String token) {

        Date expiration = Jwts.parserBuilder().setSigningKey(key)
            .build().parseClaimsJws(token).getBody().getExpiration();

        long now = new Date().getTime();
        return expiration.getTime() - now;
    }


}
