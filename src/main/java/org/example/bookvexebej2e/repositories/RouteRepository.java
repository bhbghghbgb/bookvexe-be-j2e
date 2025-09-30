package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface RouteRepository extends JpaRepository<RouteDbModel, Integer> {

    // Location-based queries
    Page<RouteDbModel> findByStartLocationContainingIgnoreCase(String startLocation, Pageable pageable);

    Page<RouteDbModel> findByEndLocationContainingIgnoreCase(String endLocation, Pageable pageable);

    // Combined location search
    Page<RouteDbModel> findByStartLocationContainingIgnoreCaseAndEndLocationContainingIgnoreCase(String startLocation
        , String endLocation, Pageable pageable);

    // Distance-based queries
    Page<RouteDbModel> findByDistanceKmBetween(BigDecimal minDistance, BigDecimal maxDistance, Pageable pageable);

    Page<RouteDbModel> findByDistanceKmLessThanEqual(BigDecimal maxDistance, Pageable pageable);

    // Duration-based queries
    Page<RouteDbModel> findByEstimatedDurationBetween(Integer minDuration, Integer maxDuration, Pageable pageable);

    Page<RouteDbModel> findByEstimatedDurationLessThanEqual(Integer maxDuration, Pageable pageable);
}
