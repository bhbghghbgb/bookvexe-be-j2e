package org.example.bookvexebej2e.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.dto.auth.AuthResponse;
import org.example.bookvexebej2e.services.auth.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final String adminRedirectUrl = "http://localhost:5173/oauth/callback"; // Admin Frontend
    private final String userRedirectUrl = "http://localhost:5173/oauth/callback"; // User Frontend

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        AuthResponse authResponse = authService.processOAuth2Login(oauth2User);

        // Determine redirect URL based on role
        Set<String> roles = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        String targetBaseUrl;
        if (roles.contains("ROLE_ADMIN")) {
            targetBaseUrl = adminRedirectUrl;
        } else {
            targetBaseUrl = userRedirectUrl;
        }

        // Build redirect URL with tokens
        String targetUrl = UriComponentsBuilder.fromUriString(targetBaseUrl)
            .queryParam("accessToken", authResponse.getAccessToken())
            .queryParam("refreshToken", authResponse.getRefreshToken())
            .build()
            .toUriString();

        // Redirect the user
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}