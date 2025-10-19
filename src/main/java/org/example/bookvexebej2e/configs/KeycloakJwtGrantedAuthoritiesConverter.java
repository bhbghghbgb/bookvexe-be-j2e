package org.example.bookvexebej2e.configs;

// Converter that extracts roles from Keycloak's JWT structure
//class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
//
//    @Override
//    public Collection<GrantedAuthority> convert(Jwt jwt) {
//        // Extract realm-level roles
//        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
//        Collection<String> realmRoles = (realmAccess != null) ? (Collection<String>) realmAccess.get(
//            "roles") : Collections.emptyList();
//
//        // Extract resource-specific roles (for a particular client)
//        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
//        // Replace "your-spring-boot-client-id" with your actual Keycloak client ID
//        Map<String, Object> clientAccess = (resourceAccess != null) ? (Map<String, Object>) resourceAccess.get(
//            "smb-j2e-swagger-client") : null;
//        Collection<String> clientRoles = (clientAccess != null) ? (Collection<String>) clientAccess.get(
//            "roles") : Collections.emptyList();
//
//        // Combine realm and client roles
//        return Stream.concat(realmRoles.stream(), clientRoles.stream())
//            .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role))
//            .collect(Collectors.toList());
//    }
//}