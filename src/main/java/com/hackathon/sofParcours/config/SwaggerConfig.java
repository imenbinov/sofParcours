package com.hackathon.sofParcours.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI sofParcoursAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SofParcours API")
                        .description("API de SofParcours avec intégration IA GPT-5")
                        .version("1.0"));
    }
}
