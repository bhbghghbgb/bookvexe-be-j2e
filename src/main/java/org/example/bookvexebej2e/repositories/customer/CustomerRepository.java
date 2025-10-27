package org.example.bookvexebej2e.repositories.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerDbModel> {
    Optional<CustomerDbModel> findByEmailAndIsDeletedFalse(@NotBlank @Email String email);

    Optional<CustomerDbModel> findByPhoneAndIsDeletedFalse(@NotBlank String phone);

    boolean existsByPhoneAndIsDeletedFalse(@NotBlank String phone);
}