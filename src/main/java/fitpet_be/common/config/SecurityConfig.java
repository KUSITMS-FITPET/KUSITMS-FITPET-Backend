package fitpet_be.common.config;

import static org.springframework.security.config.Customizer.withDefaults;

import fitpet_be.infrastructure.jwt.JwtAccessDeniedHandler;
import fitpet_be.infrastructure.jwt.JwtAuthenticationEntryPoint;
import fitpet_be.infrastructure.jwt.JwtAuthenticationFilter;
import fitpet_be.infrastructure.jwt.JwtExceptionFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] WHITE_LIST_URL = {
        // Application URLs
        "/api/v1/reviews/**",
        "/api/v1/estimates/**",
        "/api/v1/cardnews/**",
        "/api/v1/faqs/**",
        "/api/v1/partnership/**",
        "/api/v1/fitpetAdmin/login/**",

        // Swagger URLs
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/favicon.ico"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
            .requestMatchers(WHITE_LIST_URL);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults())  // CORS 설정 적용
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize ->
                authorize.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .requestMatchers("/api/v1/fitpetAdmin/master/**").hasRole("MASTER")
                    .requestMatchers("/api/v1/fitpetAdmin/estimates/**").hasRole("ESTIMATES")
                    .requestMatchers("/api/v1/fitpetAdmin/cardNews/**").hasRole("CONTENTS")
                    .requestMatchers("/api/v1/fitpetAdmin/url/**").hasRole("SITES")
                    .anyRequest().permitAll())  // 나머지 요청은 인증 없이 접근 가능
            .exceptionHandling(handler ->
                handler.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));

        // 모든 메서드 허용
        configuration.setAllowedMethods(List.of("*"));

        // 모든 헤더 허용
        configuration.setAllowedHeaders(List.of("*"));

        // 응답 헤더에서도 모든 헤더를 노출
        configuration.addExposedHeader("*");

        // 자격 증명 허용
        configuration.setAllowCredentials(true);

        // 캐시 시간 설정
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}