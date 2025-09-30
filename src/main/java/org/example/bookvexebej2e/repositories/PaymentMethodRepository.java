package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.PaymentMethodDbModel;
import org.example.bookvexebej2e.repositories.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends SoftDeleteRepository<PaymentMethodDbModel, Integer> {
    Optional<PaymentMethodDbModel> findByMethodName(String methodName);

    List<PaymentMethodDbModel> findByMethodNameContainingIgnoreCase(String methodName);
}
