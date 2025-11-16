package org.example.bookvexebej2e.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Maps the 'microservices' section of application.yaml.
 * This allows conditional enabling of external service calls.
 */
@Configuration
@ConfigurationProperties(prefix = "microservices")
@Data
public class MicroserviceProperties {
    
    // Default enabled status for both is 'false' if not specified in YAML
    private Mail mail = new Mail();
    private Notification notification = new Notification();

    @Data
    public static class Mail {
        private boolean enabled = false;
    }

    @Data
    public static class Notification {
        private boolean enabled = false;
    }
}