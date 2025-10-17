package org.example.bookvexebej2e.repository.user;


import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserDbModel> {
}
