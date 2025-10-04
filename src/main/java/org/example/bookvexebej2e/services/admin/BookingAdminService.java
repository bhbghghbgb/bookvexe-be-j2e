package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.requests.BookingQueryRequest;
import org.example.bookvexebej2e.repositories.BookingRepository;
import org.example.bookvexebej2e.repositories.BookingSeatRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingAdminService extends BaseAdminService<BookingDbModel, Integer, BookingQueryRequest> {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @Override
    protected JpaRepository<BookingDbModel, Integer> getRepository() {
        return bookingRepository;
    }

    @Override
    protected Specification<BookingDbModel> buildSpecification(BookingQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by user ID
            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("userId"), request.getUserId()));
            }

            // Filter by trip ID
            if (request.getTripId() != null) {
                predicates.add(cb.equal(root.get("trip").get("tripId"), request.getTripId()));
            }

            // Filter by single status
            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("bookingStatus"), request.getStatus()));
            }

            // Filter by multiple statuses
            if (!CollectionUtils.isEmpty(request.getStatuses())) {
                predicates.add(root.get("bookingStatus").in(request.getStatuses()));
            }

            // Filter by created date range
            if (request.getCreatedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getCreatedAfter()));
            }

            if (request.getCreatedBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getCreatedBefore()));
            }

            // Filter by price range
            if (request.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalPrice"), request.getMinPrice()));
            }

            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalPrice"), request.getMaxPrice()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    /**
     * Lấy danh sách ghế của booking
     */
    public List<BookingSeatDbModel> getBookingSeats(Integer bookingId) {
        return bookingRepository.findById(bookingId)
            .map(bookingSeatRepository::findByBooking)
            .orElse(Collections.emptyList());
    }
}
