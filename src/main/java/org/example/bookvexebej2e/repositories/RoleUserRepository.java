package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleUserRepository extends SoftDeleteRepository<RoleUserDbModel, Integer> {
    List<RoleUserDbModel> findByUser(UserDbModel user);

    List<RoleUserDbModel> findByRole(RoleDbModel role);

    Optional<RoleUserDbModel> findByUserAndRole(UserDbModel user, RoleDbModel role);

    boolean existsByRoleId(Integer roleId);

    void deleteByUser(UserDbModel user);
}