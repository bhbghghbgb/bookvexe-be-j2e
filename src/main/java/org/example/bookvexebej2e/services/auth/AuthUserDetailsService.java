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

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDbModel user = userRepository.findByUsernameAndNotDeleted(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Get roles from database
        var authorities = userRepository.findUserRolesString(user.getId())
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());

        return new AuthUserDetails(user, authorities);
    }
}
