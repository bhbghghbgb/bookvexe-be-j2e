package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.requests.RouteQueryRequest;
import org.example.bookvexebej2e.repositories.RouteRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RouteAdminService extends BaseAdminService<RouteDbModel, Integer> {

    private final RouteRepository routeRepository;

    @Override
    protected JpaRepository<RouteDbModel, Integer> getRepository() {
        return routeRepository;
    }

    public Page<RouteDbModel> findRoutesByCriteria(RouteQueryRequest queryRequest) {
        Pageable pageable = queryRequest.toPageable();

        if (StringUtils.hasText(queryRequest.getStartLocation()) && StringUtils.hasText(
            queryRequest.getEndLocation())) {
            return routeRepository.findByStartLocationContainingIgnoreCaseAndEndLocationContainingIgnoreCase(
                queryRequest.getStartLocation(), queryRequest.getEndLocation(), pageable);
        }

        if (StringUtils.hasText(queryRequest.getStartLocation())) {
            return routeRepository.findByStartLocationContainingIgnoreCase(queryRequest.getStartLocation(), pageable);
        }

        if (StringUtils.hasText(queryRequest.getEndLocation())) {
            return routeRepository.findByEndLocationContainingIgnoreCase(queryRequest.getEndLocation(), pageable);
        }

        if (queryRequest.getMinDistance() != null && queryRequest.getMaxDistance() != null) {
            return routeRepository.findByDistanceKmBetween(queryRequest.getMinDistance(), queryRequest.getMaxDistance(),
                pageable);
        }

        if (queryRequest.getMaxDistance() != null) {
            return routeRepository.findByDistanceKmLessThanEqual(queryRequest.getMaxDistance(), pageable);
        }

        if (queryRequest.getMinDuration() != null && queryRequest.getMaxDuration() != null) {
            return routeRepository.findByEstimatedDurationBetween(queryRequest.getMinDuration(),
                queryRequest.getMaxDuration(), pageable);
        }

        if (queryRequest.getMaxDuration() != null) {
            return routeRepository.findByEstimatedDurationLessThanEqual(queryRequest.getMaxDuration(), pageable);
        }

        return routeRepository.findAll(pageable);
    }
}
