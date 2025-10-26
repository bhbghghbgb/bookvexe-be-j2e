package org.example.bookvexebej2e.repositories.customer;

import java.util.Optional;

import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerDbModel> {
    Optional<CustomerDbModel> findByEmailAndIsDeletedFalse(@NotBlank @Email String email);

    Optional<CustomerDbModel> findByPhoneAndIsDeletedFalse(@NotBlank String phone);
}