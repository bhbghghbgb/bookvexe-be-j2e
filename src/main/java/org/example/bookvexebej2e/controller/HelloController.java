package org.example.bookvexebej2e.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello World");
    }

    @GetMapping("/token")
    public Map<String, Object> token(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }

    @GetMapping("/roles")
    public List<String> roles(Authentication auth) {
        return auth.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
    }
}