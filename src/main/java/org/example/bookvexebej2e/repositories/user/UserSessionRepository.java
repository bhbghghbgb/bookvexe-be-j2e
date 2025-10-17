package org.example.bookvexebej2e.repositories.user;


import org.example.bookvexebej2e.models.db.UserSessionDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends BaseRepository<UserSessionDbModel> {
}
