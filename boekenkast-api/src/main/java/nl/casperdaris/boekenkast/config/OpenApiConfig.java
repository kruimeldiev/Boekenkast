package nl.casperdaris.boekenkast.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/// Configuration class for OpenAPI 3.0
@OpenAPIDefinition(info = @Info(title = "Boekenkast API", version = "1.0.0"), servers = {
        @Server(url = "http://localhost:8080/api/v1", description = "Local server")
}, security = { @SecurityRequirement(name = "bearer") })
@SecurityScheme(name = "bearer", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}
