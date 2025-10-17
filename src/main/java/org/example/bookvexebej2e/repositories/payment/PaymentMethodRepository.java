package org.example.bookvexebej2e.repositories.payment;


import org.example.bookvexebej2e.models.db.PaymentMethodDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends BaseRepository<PaymentMethodDbModel> {
}
