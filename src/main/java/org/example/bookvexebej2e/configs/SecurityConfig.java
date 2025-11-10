package org.example.bookvexebej2e.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.services.auth.AuthUserDetailsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AuthUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint unauthorizedHandler;
    private final AuthenticationSuccessHandler oauth2SuccessHandler;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.admin.allow-all:false}")
    private boolean allowAllAdminAccess;


    /**
     * Defines the authentication mechanism using DAO (Database Access Object)
     * which relies on UserDetailsService and PasswordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Exposes the AuthenticationManager bean. Required for explicit authentication
     * (e.g., in a login endpoint using username and password).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the main security filter chain.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           ObjectProvider<ClientRegistrationRepository> clientRegistrations) throws Exception {
        http
                // CORS and CSRF Configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // EXPLICITLY SET THE ENTRY POINT TO RETURN 401 ON AUTH FAILURE
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(unauthorizedHandler))

                // 1. CONDITIONAL AUTHORIZATION LOGIC
                .authorizeHttpRequests(authz -> {
                    if (allowAllAdminAccess) {
                        // DEV MODE: Allow ALL requests (authenticated or not)
                        authz.requestMatchers("/**")
                                .permitAll();
                    } else {
                        // PROD MODE: Standard authorization rules
                        authz
                                // Permit public/auth/swagger/root

                                .requestMatchers("/swagger/**", "/swagger/v1/**", "/", "/hello", "/auth/**",
                                        "/swagger-ui/**", "/v3/api-docs/**", "/error", "/dev/*", "/api/chat/*",
                                        "/api/v1/routes/**", "/api/v1/trips/**")
                                .permitAll()
                                // Require ADMIN role for /admin/**
                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")
                                // All others require authentication
                                .anyRequest()
                                .authenticated();
                    }
                })

                // 5. Configure OAuth2 Login (using modern lambda syntax)
                //            .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/auth/oauth2/success")
                //                .failureUrl("/auth/oauth2/failure"))

                // 6. Set the authentication provider
                .authenticationProvider(authenticationProvider())

                // 7. Add the custom JWT filter BEFORE the standard UsernamePasswordAuthenticationFilter
                // This ensures JWT processing happens before Spring tries to look for form data/session.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Only enable oauth2Login if OAuth2 clients are present
        if (clientRegistrations.getIfAvailable() != null) {
            http.oauth2Login(oauth2 -> oauth2.successHandler(oauth2SuccessHandler)
                    .failureUrl("/auth/oauth2/failure"));
            log.info("OAuth2 login enabled (client registrations found).");
        } else {
            log.warn("OAuth2 login disabled (no client registrations found).");
        }

        return http.build();
    }

    /**
     * Defines the global CORS configuration allowing all requests.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",  // BMS User
            "http://localhost:5174",  // BMS Admin
            "http://localhost:5182",
            "http://localhost:8080"
        )); 
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    //    // Custom converter to map Keycloak roles
    //    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
    //        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    //        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
    //        return converter;
    //    }
}
