package org.example.bookvexebej2e.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // Springdoc auto-scans your controllers, so no extra config is required unless you want to customize

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("keycloak"))
            .components(new Components().addSecuritySchemes("keycloak",
                new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                    .flows(new OAuthFlows().authorizationCode(new OAuthFlow().authorizationUrl(
                            "http://localhost:8081/realms/smb-j2e/protocol/openid-connect/auth")
                        .tokenUrl("http://localhost:8081/realms/smb-j2e/protocol/openid-connect/token")
                        .scopes(new Scopes().addString("openid", "Access basic profile")
                            .addString("profile", "Access user profile")
                            .addString("email", "Access email"))))));
    }
}
