package org.example.bookvexebej2e.repositories.role;


import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<RoleDbModel> {
    @Query("SELECT r FROM RoleDbModel r WHERE r.code = :code AND (r.isDeleted = false OR r.isDeleted IS NULL)")
    Optional<RoleDbModel> findByCode(@Param("code") String code);

}
