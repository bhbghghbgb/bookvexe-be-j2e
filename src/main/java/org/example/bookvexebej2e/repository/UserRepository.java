package org.example.bookvexebej2e.repository;


import org.example.bookvexebej2e.models.db.UserDbModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserDbModel> {
}
