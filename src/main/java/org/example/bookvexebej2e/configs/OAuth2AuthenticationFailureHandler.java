package org.example.bookvexebej2e.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String failureRedirectUrl = "http://localhost:5173/login";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String targetUrl = UriComponentsBuilder.fromUriString(failureRedirectUrl)
                .queryParam("error", "oauth_failed")
                .queryParam("message", exception.getMessage())
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
