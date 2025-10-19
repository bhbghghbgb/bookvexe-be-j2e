package org.example.bookvexebej2e.services.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.configs.JwtUtils;
import org.example.bookvexebej2e.exceptions.UnauthorizedException;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.auth.AuthResponse;
import org.example.bookvexebej2e.models.dto.auth.LoginRequest;
import org.example.bookvexebej2e.models.dto.auth.PasswordResetConfirmRequest;
import org.example.bookvexebej2e.models.dto.auth.PasswordResetRequest;
import org.example.bookvexebej2e.repositories.auth.TokenRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        UserDbModel user = userRepository.findByUsernameAndNotDeleted(request.getUsername())
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        // Revoke existing tokens
        tokenService.revokeAllUserTokens(user.getId(), "ACCESS");
        tokenService.revokeAllUserTokens(user.getId(), "REFRESH");

        // Create new tokens
        var accessToken = tokenService.createAccessToken(user);
        var refreshToken = tokenService.createRefreshToken(user);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken.getToken());
        response.setRefreshToken(refreshToken.getToken());
        response.setExpiresIn(jwtUtils.getRefreshExpirationMs() / 1000);

        return response;
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenService.validateToken(refreshToken, "REFRESH")) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        UUID userId = jwtUtils.getUserIdFromToken(refreshToken);
        UserDbModel user = userRepository.findById(userId)
            .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Revoke old tokens
        tokenService.revokeAllUserTokens(user.getId(), "ACCESS");

        // Create new access token
        var newAccessToken = tokenService.createAccessToken(user);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(newAccessToken.getToken());
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtUtils.getRefreshExpirationMs() / 1000);

        return response;
    }

    public void requestPasswordResetAsCustomer(PasswordResetRequest request) {
        UserDbModel user = userRepository.findByCustomerEmail(request.getEmail())
            .orElse(null); // Don't reveal if email exists or not

        if (user != null && !user.getIsGoogle()) {
            // Revoke existing reset tokens
            tokenService.revokeAllUserTokens(user.getId(), "RESET_PASSWORD");

            var resetToken = tokenService.createPasswordResetToken(user);

            // Print reset code to console (in production, send email)
            log.info("Password reset token for {}: {}", user.getUsername(), resetToken.getToken());
        }
    }

    public void confirmPasswordResetAsCustomer(PasswordResetConfirmRequest request) {
        if (!tokenService.validateToken(request.getToken(), "RESET_PASSWORD")) {
            throw new UnauthorizedException("Invalid or expired reset token");
        }

        UUID userId = getUserIdFromResetToken(request.getToken());
        UserDbModel user = userRepository.findById(userId)
            .orElseThrow(() -> new UnauthorizedException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Revoke the used reset token
        tokenService.revokeAllUserTokens(user.getId(), "RESET_PASSWORD");
    }

    public void logout(String accessToken) {
        if (jwtUtils.validateToken(accessToken)) {
            UUID userId = jwtUtils.getUserIdFromToken(accessToken);
            tokenService.revokeAllUserTokens(userId, "ACCESS");
            tokenService.revokeAllUserTokens(userId, "REFRESH");
        }
    }

    private UUID getUserIdFromResetToken(String token) {
        // For reset tokens, we need to get user ID from database
        return tokenRepository.findByTokenAndTokenTypeAndRevokedFalse(token, "RESET_PASSWORD")
            .map(t -> t.getUser()
                .getId())
            .orElseThrow(() -> new UnauthorizedException("Invalid reset token"));
    }
}