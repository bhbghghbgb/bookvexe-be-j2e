package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.requests.TripQueryRequest;
import org.example.bookvexebej2e.repositories.TripRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripAdminService extends BaseAdminService<TripDbModel, Integer, TripQueryRequest> {

    private final TripRepository tripRepository;

    @Override
    protected JpaRepository<TripDbModel, Integer> getRepository() {
        return tripRepository;
    }

    @Override
    protected Specification<TripDbModel> buildSpecification(TripQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by route ID
            if (request.getRouteId() != null) {
                predicates.add(cb.equal(root.get("route").get("routeId"), request.getRouteId()));
            }

            // Filter by bus/car ID
            if (request.getBusId() != null) {
                predicates.add(cb.equal(root.get("bus").get("carId"), request.getBusId()));
            }

            // Filter by departure time range
            if (request.getDepartureAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("departureTime"), request.getDepartureAfter()));
            }

            if (request.getDepartureBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("departureTime"), request.getDepartureBefore()));
            }

            // Filter by price range
            if (request.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }

            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }

            // Filter by minimum available seats
            if (request.getMinSeats() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("availableSeats"), request.getMinSeats()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    /**
     * Tìm các chuyến đi trong tương lai có ghế trống
     */
    public List<TripDbModel> findFutureTripsWithAvailableSeats(Integer minSeats) {
        Specification<TripDbModel> spec = (root, query, cb) -> cb.and(
            cb.greaterThan(root.get("departureTime"), LocalDateTime.now()),
            cb.greaterThanOrEqualTo(root.get("availableSeats"), minSeats)
        );

        return tripRepository.findAll(spec);
    }
}
