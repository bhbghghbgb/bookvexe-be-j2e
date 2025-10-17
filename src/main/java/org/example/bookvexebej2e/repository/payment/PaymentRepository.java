package org.example.bookvexebej2e.repository.payment;


import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends BaseRepository<PaymentDbModel> {
}
