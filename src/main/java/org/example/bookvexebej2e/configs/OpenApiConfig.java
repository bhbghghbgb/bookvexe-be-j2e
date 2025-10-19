package org.example.bookvexebej2e.configs;

// OpenApiConfig.java (using springdoc-openapi)

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer globalResponsesCustomizer() {

        // 1. Resolve the schema for your custom error object
        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
            .resolveAsResolvedSchema(new AnnotatedType(CustomErrorResponse.class));

        // 2. Define the content for the error response (JSON media type)
        Content content = new Content().addMediaType("application/json", new MediaType().schema(resolvedSchema.schema));

        // 3. Define the reusable 500 and 404 response objects
        ApiResponse internalServerErrorResponse = new ApiResponse().description(
                "Internal Server Error (e.g., unexpected exception)")
            .content(content);

        ApiResponse notFoundResponse = new ApiResponse().description(
                "Resource Not Found (e.g., requested ID not found)")
            .content(content);

        // 4. Return the customizer implementation
        return openApi -> {
            // Iterate over all paths/endpoints
            openApi.getPaths()
                .values()
                .forEach(pathItem -> pathItem.readOperations()
                    .forEach(operation -> {
                        ApiResponses responses = operation.getResponses();

                        // Add 500 response if not already present
                        if (responses.get("500") == null) {
                            responses.addApiResponse("500", internalServerErrorResponse);
                        }

                        // Add 404 response if not already present
                        if (responses.get("404") == null) {
                            responses.addApiResponse("404", notFoundResponse);
                        }
                    }));
        };
    }
}
