package org.example.bookvexebej2e.repositories.auth;

import org.example.bookvexebej2e.models.db.TokenDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenDbModel, UUID> {

    Optional<TokenDbModel> findByTokenAndTokenTypeAndRevokedFalse(String token, String tokenType);

    List<TokenDbModel> findAllByUserIdAndTokenTypeAndRevokedFalse(UUID userId, String tokenType);

    @Modifying
    @Query("UPDATE TokenDbModel t SET t.revoked = true, t.updatedDate = CURRENT_TIMESTAMP WHERE t.user.id = :userId " + "AND t.tokenType = :tokenType AND t.revoked = false")
    void revokeAllUserTokensByType(@Param("userId") UUID userId, @Param("tokenType") String tokenType);

    @Modifying
    @Query("DELETE FROM TokenDbModel t WHERE t.expiresAt < :currentTime")
    void deleteExpiredTokens(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT t FROM TokenDbModel t WHERE t.user.id = :userId AND t.tokenType = 'RESET_PASSWORD' AND t.revoked " +
        "=" + " false AND t.expiresAt > CURRENT_TIMESTAMP ORDER BY t.createdDate DESC")
    Optional<TokenDbModel> findValidPasswordResetToken(@Param("userId") UUID userId);
}
