package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleDbModel, Integer> {
    Optional<RoleDbModel> findByCode(String code);

    List<RoleDbModel> findByNameContainingIgnoreCase(String name);
}