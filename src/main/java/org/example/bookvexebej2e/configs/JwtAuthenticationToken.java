package org.example.bookvexebej2e.configs;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Represents an authenticated user derived from a JWT.
 * Principal is AuthUserDetails. Credentials are not relevant (null).
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUserDetails principal;

    public JwtAuthenticationToken(AuthUserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        // Mark the token as authenticated immediately after construction
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // Credentials (password) are not applicable
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public String getName() {
        return ((AuthUserDetails) getPrincipal()).getUsername();
    }

    // You can skip this method, as the token is always marked authenticated in the constructor
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (!isAuthenticated) {
            // Only allow setting to false if you needed a separate unauthenticated state,
            // but for JWT, we usually throw an error if someone tries to un-authenticate a validated token.
            throw new IllegalArgumentException("Cannot unauthenticate a validated JwtAuthenticationToken");
        }
        super.setAuthenticated(true);
    }
}