package org.example.bookvexebej2e.repository;


import org.example.bookvexebej2e.models.db.PasswordResetTokenDbModel;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetTokenDbModel> {
}
