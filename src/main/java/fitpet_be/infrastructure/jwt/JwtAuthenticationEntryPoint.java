package fitpet_be.infrastructure.jwt;

import static fitpet_be.infrastructure.utils.ExceptionHandlerUtil.exceptionHandler;

import fitpet_be.common.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        ErrorStatus exception = (ErrorStatus) request.getAttribute("exception");
        log.info("===================== EntryPoint - Exception Control : " + request.getAttribute(
            "exception"));

        if (exception.equals(ErrorStatus._JWT_NOT_FOUND)) {
            try {
                exceptionHandler(response, ErrorStatus._JWT_NOT_FOUND,
                    HttpServletResponse.SC_UNAUTHORIZED);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else if (exception.equals(ErrorStatus._JWT_INVALID)) {
            try {
                exceptionHandler(response, ErrorStatus._JWT_INVALID, HttpServletResponse.SC_UNAUTHORIZED);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else if (exception.equals(ErrorStatus._JWT_EXPIRED)) {
            try {
                exceptionHandler(response, ErrorStatus._JWT_EXPIRED, HttpServletResponse.SC_UNAUTHORIZED);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
