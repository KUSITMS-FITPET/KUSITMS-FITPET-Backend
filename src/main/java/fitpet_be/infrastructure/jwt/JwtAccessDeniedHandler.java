package fitpet_be.infrastructure.jwt;

import static fitpet_be.infrastructure.utils.ExceptionHandlerUtil.exceptionHandler;

import fitpet_be.common.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        try {
            exceptionHandler(response, ErrorStatus._FORBIDDEN, HttpServletResponse.SC_FORBIDDEN);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}