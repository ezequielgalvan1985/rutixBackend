package elementary.Rutix.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de OpenAPI/Swagger para Rutix.
 * UI disponible en /swagger-ui/index.html
 * Spec JSON en /v3/api-docs
 *
 * La API usa JWT (header Authorization: Bearer <token>), emitido por el
 * servicio de seguridad del ecosistema. Mientras SecurityConfig permita
 * todas las requests (permitAll), el token es opcional para probar los
 * endpoints, pero se documenta el esquema para cuando se habilite la
 * autorizacion real.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI rutixOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rutix API")
                        .description("API de Rutix: viajes compartidos en auto, reservas y pagos entre perfiles.")
                        .version("v1")
                        .contact(new Contact().name("Rutix")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
