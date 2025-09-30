package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.db.UserSessionDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessionDbModel, Integer> {
    Optional<UserSessionDbModel> findByAccessToken(String accessToken);

    List<UserSessionDbModel> findByUser(UserDbModel user);

    List<UserSessionDbModel> findByRevoked(Boolean revoked);

    List<UserSessionDbModel> findByExpiresAtBefore(LocalDateTime date);
}
