package org.example.bookvexebej2e.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider authenticationProvider; // USE THE PROVIDER

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null) {

                // 1. Use the centralized provider to validate and get the Authentication token
                Authentication authentication = authenticationProvider.getAuthentication(jwt);

                if (authentication != null) {
                    // 2. Set request details and put into Security Context
                    if (authentication instanceof AbstractAuthenticationToken abstractAuth) {
                        // Set web details for tracing (applies to both JwtAuthenticationToken and others)
                        abstractAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    }

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    // Token was invalid, expired, wrong type, or revoked (handled inside provider)
                    log.warn("JWT rejected (invalid, expired, or revoked).");
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}