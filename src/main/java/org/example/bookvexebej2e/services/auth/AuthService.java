package org.example.bookvexebej2e.services.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.configs.JwtUtils;
import org.example.bookvexebej2e.exceptions.UnauthorizedException;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.auth.*;
import org.example.bookvexebej2e.repositories.auth.TokenRepository;
import org.example.bookvexebej2e.repositories.customer.CustomerRepository;
import org.example.bookvexebej2e.repositories.customer.CustomerTypeRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final CustomerRepository customerRepository;
    private final CustomerTypeRepository customerTypeRepository;
    private final TokenService tokenService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        Optional<UserDbModel> userOptional;

        if (Boolean.TRUE.equals(request.getLoginAsAdmin())) {
            // Admin login with username
            userOptional = userRepository.findAdminByUsernameAndNotDeleted(request.getUsername());
        } else {
            // Customer login with phone number (which is the username)
            userOptional = userRepository.findByUsernameAndNotDeleted(request.getUsername());

            // Additional check: ensure the user is a customer and has the phone set as username
            if (userOptional.isPresent() && userOptional.get().getCustomer() == null) {
                throw new UnauthorizedException("Invalid credentials");
            }
        }

        UserDbModel user = userOptional
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return issueJwt(user);
    }

    @Transactional
    public AuthResponse processOAuth2Login(OAuth2User oauth2User) {
        String googleId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (googleId == null) {
            throw new RuntimeException("Google OAuth2 malfunction");
        }

        if (email == null) {
            throw new RuntimeException("Email not provided by Google OAuth2");
        }

        // Check for existing UserDbModel linked to this google account
        Optional<UserDbModel> existingUserOpt = userRepository.findByGoogleAccountAndIsDeletedFalse(googleId);
        UserDbModel user;

        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();

            // Update user information if needed
            if (!email.equals(user.getCustomer().getEmail())) {
                user.getCustomer().setEmail(email);
                customerRepository.save(user.getCustomer());
            }
            if (name != null && !name.equals(user.getCustomer().getName())) {
                user.getCustomer().setName(name);
                customerRepository.save(user.getCustomer());
            }
        } else {
            // Create new customer and user
            CustomerDbModel customer = new CustomerDbModel();
            customer.setCode("GGL_" + googleId);
            customer.setName(name != null ? name : "Google User");
            customer.setEmail(email);
            customer.setPhone(null); // No phone from Google - user must set this
            customer.setDescription("Auto-created via Google OAuth2");
            customer.setCustomerType(null);

            CustomerDbModel savedCustomer = customerRepository.save(customer);

            user = new UserDbModel();
            user.setUsername(null); // Set to null initially - user must set phone as username
            user.setPassword(null); // No password for Google account initially
            user.setIsGoogle(true);
            user.setGoogleAccount(googleId);
            user.setCustomer(savedCustomer);

            UserDbModel savedUser = userRepository.save(user);

            // Update customer with user reference
            savedCustomer.setUser(savedUser);
            customerRepository.save(savedCustomer);

            log.info("New Customer and User created via Google OAuth2: {}", email);
        }

        return issueJwt(user);
    }

    private AuthResponse issueJwt(UserDbModel user) {
        // Revoke existing tokens
        tokenService.revokeAllUserTokens(user.getId(), "ACCESS");
        tokenService.revokeAllUserTokens(user.getId(), "REFRESH");

        // Create new tokens
        var accessToken = tokenService.createAccessToken(user);
        var refreshToken = tokenService.createRefreshToken(user);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken.getToken());
        response.setRefreshToken(refreshToken.getToken());
        // Use the access token expiration for the client-side expiresIn
        response.setExpiresIn(jwtUtils.getRefreshExpirationMs() / 1000);

        return response;
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenService.validateTokenIsValid(refreshToken, "REFRESH")) {
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
        if (!tokenService.validateTokenIsValid(request.getToken(), "RESET_PASSWORD")) {
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

    public void changePassword(String accessToken, ChangePasswordRequest request) {
        if (!jwtUtils.validateToken(accessToken)) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID userId = jwtUtils.getUserIdFromToken(accessToken);
        UserDbModel user = userRepository.findById(userId)
            .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Update to new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Revoke all tokens for security
        tokenService.revokeAllUserTokens(user.getId(), "ACCESS");
        tokenService.revokeAllUserTokens(user.getId(), "REFRESH");
    }


    private UUID getUserIdFromResetToken(String token) {
        // For reset tokens, we need to get user ID from database
        return tokenRepository.findByTokenAndTokenTypeAndRevokedFalse(token, "RESET_PASSWORD")
            .map(t -> t.getUser()
                .getId())
            .orElseThrow(() -> new UnauthorizedException("Invalid reset token"));
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists with this email as username
        if (userRepository.findByUsernameAndNotDeleted(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // Check if customer with this email already exists
        if (customerRepository.findByEmailAndIsDeletedFalse(request.getEmail()).isPresent()) {
            throw new RuntimeException("Customer with this email already exists");
        }

        // Check if customer with this phone already exists
        if (customerRepository.findByPhoneAndIsDeletedFalse(request.getPhone()).isPresent()) {
            throw new RuntimeException("Customer with this phone already exists");
        }

        // Create customer
        CustomerDbModel customer = new CustomerDbModel();
        customer.setCode(generateCustomerCode());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setDescription("Registered via website");
        customer.setCustomerType(null); // No customer type for new registrations

        CustomerDbModel savedCustomer = customerRepository.save(customer);

        // Create user
        UserDbModel user = new UserDbModel();
        user.setUsername(request.getPhone()); // Use phone as username for login
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsGoogle(false);
        user.setGoogleAccount(null);
        user.setCustomer(savedCustomer);

        UserDbModel savedUser = userRepository.save(user);

        // Update customer with user reference
        savedCustomer.setUser(savedUser);
        customerRepository.save(savedCustomer);

        log.info("New customer registered: {}", request.getEmail());

        // Issue JWT tokens
        return issueJwt(savedUser);
    }

    private String generateCustomerCode() {
        // Generate a unique customer code like CUST_123456
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "CUST_" + timestamp.substring(timestamp.length() - 6);
    }
}