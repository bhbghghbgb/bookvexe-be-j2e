package org.example.bookvexebej2e.repository.password;


import org.example.bookvexebej2e.models.db.PasswordResetTokenDbModel;
import org.example.bookvexebej2e.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetTokenDbModel> {
}
