package org.example.bookvexebej2e.repositories.user;


import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<UserDbModel> {
    @Query("SELECT u FROM UserDbModel u WHERE u.username = :username AND (u.isDeleted = false OR u.isDeleted IS NULL)")
    Optional<UserDbModel> findByUsernameAndNotDeleted(@Param("username") String username);

    @Query("SELECT u FROM UserDbModel u JOIN u.customer c WHERE c.email = :email")
    Optional<UserDbModel> findByCustomerEmail(@Param("email") String email);

    @Query("SELECT r.code FROM RoleDbModel r JOIN RoleUserDbModel ur ON r.id = ur.role.id WHERE ur.user.id = :userId")
    Set<String> findUserRolesString(@Param("userId") UUID userId);
}
