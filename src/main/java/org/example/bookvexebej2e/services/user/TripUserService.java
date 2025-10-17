package org.example.bookvexebej2e.services.user;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.requests.TripQueryRequest;
import org.example.bookvexebej2e.repositories.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TripUserService {

    private final TripRepository tripRepository;

    public Page<TripDbModel> search(TripQueryRequest request) {
        Pageable pageable = request.toPageable();
        Specification<TripDbModel> spec = buildSpecification(request);
        return tripRepository.findAll(spec, pageable);
    }

    private Specification<TripDbModel> buildSpecification(TripQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            if (request.getRouteId() != null) {
                predicates.add(cb.equal(root.get("route")
                    .get("routeId"), request.getRouteId()));
            }
            if (request.getBusId() != null) {
                predicates.add(cb.equal(root.get("bus")
                    .get("carId"), request.getBusId()));
            }
            if (StringUtils.hasText(request.getStartLocation())) {
                predicates.add(cb.like(cb.lower(root.get("route")
                    .get("startLocation")), "%" + request.getStartLocation()
                    .toLowerCase() + "%"));
            }
            if (StringUtils.hasText(request.getEndLocation())) {
                predicates.add(cb.like(cb.lower(root.get("route")
                    .get("endLocation")), "%" + request.getEndLocation()
                    .toLowerCase() + "%"));
            }
            if (request.getDepartureAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("departureTime"), request.getDepartureAfter()));
            }
            if (request.getDepartureBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("departureTime"), request.getDepartureBefore()));
            }
            if (request.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }
            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }
            if (request.getMinSeats() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("availableSeats"), request.getMinSeats()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
