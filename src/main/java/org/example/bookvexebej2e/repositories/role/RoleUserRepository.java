package org.example.bookvexebej2e.repositories.role;


import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleUserRepository extends BaseRepository<RoleUserDbModel> {
    @Query("SELECT ru FROM RoleUserDbModel ru WHERE ru.user.id = :userId AND ru.role.id = :roleId")
    Optional<RoleUserDbModel> findByUserIdAndRoleId(@Param("userId") UUID userId, @Param("roleId") UUID roleId);
}
