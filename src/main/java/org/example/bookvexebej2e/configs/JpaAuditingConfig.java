package org.example.bookvexebej2e.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty(); // Not authenticated (e.g., app startup/seeder)
            }

            Object principal = authentication.getPrincipal();

            // Check if the principal is your custom AuthUserDetails
            if (principal instanceof AuthUserDetails authUserDetails) {
                // Return the UUID of the user
                return Optional.of(authUserDetails.getUserId());
            }

            return Optional.empty(); // Principal is not the expected type
        };
    }
}
