package org.example.bookvexebej2e.controllers.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.dto.auth.CustomerProfileUpdate;
import org.example.bookvexebej2e.models.dto.user.UserResponse;
import org.example.bookvexebej2e.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        UserResponse user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
        @Valid @RequestBody CustomerProfileUpdate updateDto,
        Authentication authentication
    ) {
        UUID userId = (UUID) authentication.getPrincipal();
        UserResponse updatedUser = userService.updateCustomerProfile(userId, updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/setup-credentials")
    public ResponseEntity<UserResponse> setupCredentials(
        @Valid @RequestBody CustomerProfileUpdate setupDto,
        Authentication authentication
    ) {
        UUID userId = (UUID) authentication.getPrincipal();
        UserResponse updatedUser = userService.setupCustomerCredentials(userId, setupDto);
        return ResponseEntity.ok(updatedUser);
    }
}