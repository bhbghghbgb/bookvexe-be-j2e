package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.UserDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// Custom implementation for complex queries
public interface UserRepositoryCustom {
    Page<UserDbModel> findUsersByCriteria(String name, String email, Boolean active, Pageable pageable);

    List<UserDbModel> findUsersWithActiveRoles();
}