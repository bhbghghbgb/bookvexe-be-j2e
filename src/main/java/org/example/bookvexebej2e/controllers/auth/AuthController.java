package org.example.bookvexebej2e.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.configs.annotations.CurrentUser;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.auth.*;
import org.example.bookvexebej2e.services.auth.AuthService;
import org.example.bookvexebej2e.services.permission.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PermissionService permissionService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(7);
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = extractToken(request);
        authService.logout(token);
        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
        @Valid @RequestBody ChangePasswordRequest request,
        @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.substring(7);
        authService.changePassword(token, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordResetAsCustomer(@Valid @RequestBody PasswordResetRequest request) {
        authService.requestPasswordResetAsCustomer(request);
        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Void> confirmPasswordResetAsCustomer(@Valid @RequestBody PasswordResetConfirmRequest request) {
        authService.confirmPasswordResetAsCustomer(request);
        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> oauth2Success() {
        // Handle OAuth2 success - you'll need to implement this based on your OAuth2 provider
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/oauth2/failure")
    public ResponseEntity<String> oauth2Failure() {
        return ResponseEntity.badRequest()
            .body("OAuth2 authentication failed");
    }

    /**
     * API để frontend lấy thông tin user hiện tại và quyền hạn
     * Dùng để ẩn/hiện các nút thao tác và chặn truy cập routes
     */
    @GetMapping("/me")
    public ResponseEntity<UserPermissionsResponse> getCurrentUserPermissions(@CurrentUser UserDbModel user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Map<String, ModulePermission> permissions = permissionService.getAllUserPermissions(user);

        UserPermissionsResponse response = UserPermissionsResponse.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .isAdmin(user.getIsAdmin())
            .isGoogle(user.getIsGoogle())
            .permissions(permissions)
            .build();

        // Add email and name from customer or employee if available
        if (user.getCustomer() != null) {
            response.setEmail(user.getCustomer().getEmail());
            response.setName(user.getCustomer().getName());
        } else if (user.getEmployee() != null) {
            response.setEmail(user.getEmployee().getEmail());
            response.setName(user.getEmployee().getName());
        }

        return ResponseEntity.ok(response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
