package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.requests.PaymentQueryRequest;
import org.example.bookvexebej2e.repositories.PaymentRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class PaymentAdminService extends BaseAdminService<PaymentDbModel, Integer, PaymentQueryRequest> {

    private final PaymentRepository paymentRepository;

    @Override
    protected JpaRepository<PaymentDbModel, Integer> getRepository() {
        return paymentRepository;
    }

    @Override
    protected Specification<PaymentDbModel> buildSpecification(PaymentQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by booking ID
            if (request.getBookingId() != null) {
                predicates.add(cb.equal(root.get("booking").get("bookingId"), request.getBookingId()));
            }

            // Filter by payment method ID
            if (request.getMethodId() != null) {
                predicates.add(cb.equal(root.get("method").get("methodId"), request.getMethodId()));
            }

            // Filter by single status
            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            // Filter by multiple statuses
            if (!CollectionUtils.isEmpty(request.getStatuses())) {
                predicates.add(root.get("status").in(request.getStatuses()));
            }

            // Filter by amount range
            if (request.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), request.getMinAmount()));
            }

            if (request.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), request.getMaxAmount()));
            }

            // Filter by created date range
            if (request.getCreatedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getCreatedAfter()));
            }

            if (request.getCreatedBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getCreatedBefore()));
            }

            // Filter by paid date range
            if (request.getPaidAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("paidAt"), request.getPaidAfter()));
            }

            if (request.getPaidBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("paidAt"), request.getPaidBefore()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
