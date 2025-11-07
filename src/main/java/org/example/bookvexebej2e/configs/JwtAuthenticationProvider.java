package org.example.bookvexebej2e.configs;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.services.auth.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    /**
     * Validates an access token and constructs the Authentication object.
     * @param token The raw JWT string (without "Bearer ").
     * @return A valid Authentication object, or null if validation fails.
     */
    public Authentication getAuthentication(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        // 1. Check token validity and non-revoked status in DB
        if (!tokenService.validateTokenIsValid(token, "ACCESS")) {
            return null;
        }

        // 2. Parse token and get the user identifier
        String identifier = jwtUtils.getUsernameFromToken(token);

        UserDetails userDetails = null;

        try {
            // Try loading by username
            userDetails = userDetailsService.loadUserByUsername(identifier);
        } catch (UsernameNotFoundException e) {
            // If not found, try loading by UUID
            try {
                UUID id = jwtUtils.getUserIdFromToken(token);
                userDetails = userDetailsService.loadUserByUsername(id.toString());
            } catch (IllegalArgumentException | UsernameNotFoundException ignored) {
                // Not a valid UUID or user not found by ID
                return null;
            }
        }

        if (userDetails == null) {
            return null;
        }

        AuthUserDetails authUserDetails = (AuthUserDetails) userDetails;

        // 4. Create the Custom Authentication Token
        return new JwtAuthenticationToken(
            authUserDetails,
            authUserDetails.getAuthorities()
        );
    }
}
