package org.example.bookvexebej2e.configs;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.services.auth.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

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
        String username = jwtUtils.getUsernameFromToken(token);

        try {
            // Load UserDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            AuthUserDetails authUserDetails = (AuthUserDetails) userDetails;

            // 4. Create the Custom Authentication Token
            return new JwtAuthenticationToken(
                authUserDetails,
                authUserDetails.getAuthorities()
            );

        } catch (Exception e) {
            // Token is structurally valid but user not found/disabled
            return null;
        }
    }
}
