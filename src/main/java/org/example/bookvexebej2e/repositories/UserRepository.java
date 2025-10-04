package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.base.SoftDeleteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends SoftDeleteRepository<UserDbModel, Integer>, JpaSpecificationExecutor<UserDbModel> {

    // Basic unique field queries
    Optional<UserDbModel> findByEmail(String email);

    Optional<UserDbModel> findByUserUuid(UUID userUuid);

    Optional<UserDbModel> findByPhoneNumber(String phoneNumber);

    // Search with pagination
    Page<UserDbModel> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    Page<UserDbModel> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<UserDbModel> findByFullNameContainingIgnoreCaseAndIsActiveTrue(String fullName, Pageable pageable);

    // Status-based queries
    Page<UserDbModel> findByIsActive(Boolean isActive, Pageable pageable);
}
