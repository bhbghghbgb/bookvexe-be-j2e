package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.PasswordResetTokenDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenDbModel, Integer> {
    Optional<PasswordResetTokenDbModel> findByToken(String token);

    List<PasswordResetTokenDbModel> findByUser(UserDbModel user);

    List<PasswordResetTokenDbModel> findByExpiresAtBefore(LocalDateTime date);
}