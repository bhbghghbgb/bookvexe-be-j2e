package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TripRepository extends JpaRepository<TripDbModel, String>,
    JpaSpecificationExecutor<TripDbModel>,
    QuerydslPredicateExecutor<TripDbModel> {

    // Route-based queries
    Page<TripDbModel> findByRoute(RouteDbModel route, Pageable pageable);

    Page<TripDbModel> findByRoute_Id(String routeId, Pageable pageable);

    // Date-based queries
    Page<TripDbModel> findByDepartureTimeAfter(LocalDateTime after, Pageable pageable);

    Page<TripDbModel> findByDepartureTimeBefore(LocalDateTime before, Pageable pageable);

    Page<TripDbModel> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Price and seat availability
    Page<TripDbModel> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<TripDbModel> findByPriceLessThanEqual(BigDecimal maxPrice, Pageable pageable);

    Page<TripDbModel> findByAvailableSeatsGreaterThanEqual(Integer minSeats, Pageable pageable);

    // Future trips
    Page<TripDbModel> findByDepartureTimeAfterAndAvailableSeatsGreaterThanEqual(LocalDateTime after, Integer minSeats
        , Pageable pageable);
}
