package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.requests.PaymentQueryRequest;
import org.example.bookvexebej2e.repositories.PaymentRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentAdminService extends BaseAdminService<PaymentDbModel, Integer> {

    private final PaymentRepository paymentRepository;

    @Override
    protected JpaRepository<PaymentDbModel, Integer> getRepository() {
        return paymentRepository;
    }

    public Page<PaymentDbModel> findPaymentsByCriteria(PaymentQueryRequest queryRequest) {
        Pageable pageable = queryRequest.toPageable();

        if (queryRequest.getBookingId() != null) {
            return paymentRepository.findByBookingBookingId(queryRequest.getBookingId(), pageable);
        }

        if (queryRequest.getMethodId() != null) {
            return paymentRepository.findByMethodMethodId(queryRequest.getMethodId(), pageable);
        }

        if (queryRequest.getStatus() != null) {
            return paymentRepository.findByStatus(queryRequest.getStatus(), pageable);
        }

        if (queryRequest.getStatuses() != null && !queryRequest.getStatuses()
            .isEmpty()) {
            return paymentRepository.findByStatusIn(queryRequest.getStatuses(), pageable);
        }

        if (queryRequest.getMinAmount() != null && queryRequest.getMaxAmount() != null) {
            return paymentRepository.findByAmountBetween(queryRequest.getMinAmount(), queryRequest.getMaxAmount(),
                pageable);
        }

        if (queryRequest.getCreatedAfter() != null && queryRequest.getCreatedBefore() != null) {
            return paymentRepository.findByCreatedAtBetween(queryRequest.getCreatedAfter(),
                queryRequest.getCreatedBefore(), pageable);
        }

        if (queryRequest.getPaidAfter() != null && queryRequest.getPaidBefore() != null) {
            return paymentRepository.findByPaidAtBetween(queryRequest.getPaidAfter(), queryRequest.getPaidBefore(),
                pageable);
        }

        if (queryRequest.getStatus() != null && queryRequest.getCreatedAfter() != null && queryRequest.getCreatedBefore() != null) {
            return paymentRepository.findByStatusAndCreatedAtBetween(queryRequest.getStatus(),
                queryRequest.getCreatedAfter(), queryRequest.getCreatedBefore(), pageable);
        }

        return paymentRepository.findAll(pageable);
    }
}
