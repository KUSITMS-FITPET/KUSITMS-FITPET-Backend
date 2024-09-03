package fitpet_be.infrastructure.jwt;

import static fitpet_be.infrastructure.utils.JwtProperties.ACCESS_HEADER_STRING;
import static fitpet_be.infrastructure.utils.JwtProperties.TOKEN_PREFIX;

import fitpet_be.application.exception.ApiException;
import fitpet_be.common.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        if (isPublicUrl(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = null;

        String bearerToken = request.getHeader(ACCESS_HEADER_STRING);
        if (bearerToken == null || !isBearer(bearerToken)) {
            request.setAttribute("exception", ErrorStatus._JWT_NOT_FOUND);
            filterChain.doFilter(request, response);
            return;
        } else {
            accessToken = bearerToken.substring(7);
            log.info("===================== ACCESS-TOKEN : " + accessToken);

        }
        switch (jwtProvider.validateToken(accessToken)) {

            case "VALID":
                log.info("===================== LOGIN SUCCESS");
                break;

            case "INVALID":
                log.info("===================== INVALID ACCESS-TOKEN");
                request.setAttribute("exception", ErrorStatus._JWT_INVALID);
                break;

            case "EXPIRED":
                log.info("===================== EXPIRED ACCESS-TOKEN");
                request.setAttribute("exception", ErrorStatus._JWT_EXPIRED);
                break;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String requestUrl) {
        return requestUrl.equals("/api") ||
            requestUrl.equals("/api/login") ||
            requestUrl.equals("/api/signup") ||
            requestUrl.startsWith("/api/kindergartens/**") ||
            requestUrl.startsWith("/swagger-ui/**") ||
            requestUrl.startsWith("/swagger-resources/**") ||
            requestUrl.startsWith("/v3/api-docs/**") ||
            requestUrl.startsWith("/favicon.ico");
    }

    private boolean isBearer(String authorizationHeader) {
        return authorizationHeader.startsWith(TOKEN_PREFIX);
    }

}