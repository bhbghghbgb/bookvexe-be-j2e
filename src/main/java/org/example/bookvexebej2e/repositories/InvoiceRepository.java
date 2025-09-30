package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.InvoiceDbModel;
import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceDbModel, Integer> {
    Optional<InvoiceDbModel> findByInvoiceNumber(String invoiceNumber);

    Optional<InvoiceDbModel> findByPayment(PaymentDbModel payment);

    List<InvoiceDbModel> findByFileUrlIsNotNull();
}