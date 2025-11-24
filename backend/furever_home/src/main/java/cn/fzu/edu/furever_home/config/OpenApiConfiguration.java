package cn.fzu.edu.furever_home.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI api() {
        SecurityScheme token = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("satoken");
        return new OpenAPI()
                .info(new Info().title("Furever Home API").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList("satoken"))
                .components(new Components().addSecuritySchemes("satoken", token));
    }
}