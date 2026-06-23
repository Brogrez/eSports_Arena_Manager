package eSports_Arena_Manager.sanction_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String ESQUEMA = "bearer-jwt";

    // @Bean: springdoc usa este objeto para construir la pagina de Swagger UI.
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()

                .info(new Info()
                        .title("API Sanction Service")
                        .version("1.0")
                        .description("Documentación de la API de sanciones"))

                .components(new Components().addSecuritySchemes(ESQUEMA,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))

                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA));
    }
}
