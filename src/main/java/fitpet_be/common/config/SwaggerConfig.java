package fitpet_be.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .addServersItem(new Server().url("/"))  // 서버 URL 설정
            .info(apiInfo());  // API 정보 설정
    }

    private Info apiInfo() {
        return new Info()
            .title("FITPET Springdoc")
            .description("Springdoc을 사용한 FITPET API 테스트")
            .version("1.0.0");
    }
}

