package org.example.bookvexebej2e.configs;

import org.example.bookvexebej2e.models.db.UserDbModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    /**
     * Retrieves the authenticated AuthUserDetails object from the SecurityContext.
     *
     * @return The AuthUserDetails object, or null if unauthenticated or not of the expected type.
     */
    public AuthUserDetails getAuthUserDetails() {
        if (SecurityContextHolder.getContext()
            .getAuthentication() == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        if (principal instanceof AuthUserDetails authUserDetails) {
            return authUserDetails;
        }
        return null;
    }

    /**
     * Retrieves the current UserDbModel entity from the SecurityContext.
     *
     * @return The UserDbModel, or null if not authenticated.
     */
    public UserDbModel getCurrentUserEntity() {
        AuthUserDetails details = getAuthUserDetails();
        return details != null ? details.getUser() : null;
    }
}