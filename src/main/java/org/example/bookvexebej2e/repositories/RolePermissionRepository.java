package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RolePermissionDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionDbModel, Integer> {
    Optional<RolePermissionDbModel> findByRole(RoleDbModel role);
}
