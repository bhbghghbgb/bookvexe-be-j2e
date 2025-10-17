package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.requests.RouteQueryRequest;
import org.example.bookvexebej2e.repositories.RouteRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RouteAdminService extends BaseAdminService<RouteDbModel, Integer, RouteQueryRequest> {

    private final RouteRepository routeRepository;

    @Override
    protected JpaRepository<RouteDbModel, Integer> getRepository() {
        return routeRepository;
    }

    @Override
    protected Specification<RouteDbModel> buildSpecification(RouteQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by start location (partial match, case insensitive)
            if (StringUtils.hasText(request.getStartLocation())) {
                predicates.add(cb.like(cb.lower(root.get("startLocation")), "%" + request.getStartLocation()
                    .toLowerCase() + "%"));
            }

            // Filter by end location (partial match, case insensitive)
            if (StringUtils.hasText(request.getEndLocation())) {
                predicates.add(cb.like(cb.lower(root.get("endLocation")), "%" + request.getEndLocation()
                    .toLowerCase() + "%"));
            }

            // Filter by distance range
            if (request.getMinDistance() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("distanceKm"), request.getMinDistance()));
            }

            if (request.getMaxDistance() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("distanceKm"), request.getMaxDistance()));
            }

            // Filter by duration range
            if (request.getMinDuration() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("estimatedDuration"), request.getMinDuration()));
            }

            if (request.getMaxDuration() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("estimatedDuration"), request.getMaxDuration()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
