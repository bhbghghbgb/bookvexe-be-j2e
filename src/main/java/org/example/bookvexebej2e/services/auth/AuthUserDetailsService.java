package org.example.bookvexebej2e.services.auth;

import lombok.RequiredArgsConstructor;

import org.example.bookvexebej2e.configs.AuthUserDetails;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    // Load user by either UUID or username
    public UserDbModel loadUser(String identifier) {
        try {
            UUID id = UUID.fromString(identifier);
            return loadUserById(id);
        } catch (IllegalArgumentException | NullPointerException e) {
            return loadUserByUsernameInternal(identifier);
        }
    }

    // Load user by username only
    private UserDbModel loadUserByUsernameInternal(String username) {
        return userRepository.findByUsernameAndNotDeleted(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));
    }

    // Load user by UUID only
    public UserDbModel loadUserById(UUID id) {
        return userRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found by ID: " + id));
    }

    // Spring Security entry point
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDbModel user = loadUser(username);
        return buildUserDetails(user);
    }

    // Shared logic to build AuthUserDetails
    private AuthUserDetails buildUserDetails(UserDbModel user) {
        var authorities = userRepository.findUserRolesString(user.getId())
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());

        return new AuthUserDetails(user, authorities);
    }
}
