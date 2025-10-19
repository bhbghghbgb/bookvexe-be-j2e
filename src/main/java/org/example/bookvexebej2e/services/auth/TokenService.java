package org.example.bookvexebej2e.services.auth;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.configs.JwtUtils;
import org.example.bookvexebej2e.models.db.TokenDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.auth.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtUtils jwtUtils;

    public TokenDbModel createAccessToken(UserDbModel user) {
        String token = jwtUtils.generateAccessToken(user.getId(), user.getUsername());
        return createToken(user, token, "ACCESS", LocalDateTime.now()
            .plusSeconds(jwtUtils.getRefreshExpirationMs() / 1000));
    }

    public TokenDbModel createRefreshToken(UserDbModel user) {
        String token = jwtUtils.generateRefreshToken(user.getId(), user.getUsername());
        return createToken(user, token, "REFRESH", LocalDateTime.now()
            .plusSeconds(jwtUtils.getRefreshExpirationMs() / 1000));
    }

    public TokenDbModel createPasswordResetToken(UserDbModel user) {
        String token = UUID.randomUUID()
            .toString();
        return createToken(user, token, "RESET_PASSWORD", LocalDateTime.now()
            .plusHours(24));
    }

    private TokenDbModel createToken(UserDbModel user, String token, String tokenType, LocalDateTime expiresAt) {
        TokenDbModel tokenEntity = new TokenDbModel();
        tokenEntity.setUser(user);
        tokenEntity.setToken(token);
        tokenEntity.setTokenType(tokenType);
        tokenEntity.setExpiresAt(expiresAt);
        tokenEntity.setRevoked(false);
        return tokenRepository.save(tokenEntity);
    }

    public void revokeAllUserTokens(UUID userId, String tokenType) {
        tokenRepository.revokeAllUserTokensByType(userId, tokenType);
    }

    public boolean validateToken(String token, String tokenType) {
        return tokenRepository.findByTokenAndTokenTypeAndRevokedFalse(token, tokenType)
            .map(t -> !t.getExpiresAt()
                .isBefore(LocalDateTime.now()))
            .orElse(false);
    }

    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}
