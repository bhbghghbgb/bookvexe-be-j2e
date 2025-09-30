package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingDbModel, Integer>,
    QuerydslPredicateExecutor<BookingDbModel> {

    // User-based queries with pagination
    Page<BookingDbModel> findByUser(UserDbModel user, Pageable pageable);

    Page<BookingDbModel> findByUserUserId(Integer userId, Pageable pageable);

    // Trip-based queries
    Page<BookingDbModel> findByTrip(TripDbModel trip, Pageable pageable);

    Page<BookingDbModel> findByTripTripId(Integer tripId, Pageable pageable);

    // Status-based queries
    Page<BookingDbModel> findByBookingStatus(String bookingStatus, Pageable pageable);

    Page<BookingDbModel> findByBookingStatusIn(List<String> statuses, Pageable pageable);

    // Date range queries
    Page<BookingDbModel> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<BookingDbModel> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);

    Page<BookingDbModel> findByCreatedAtBefore(LocalDateTime date, Pageable pageable);

    // Combined queries
    Page<BookingDbModel> findByUserAndBookingStatus(UserDbModel user, String status, Pageable pageable);

    Page<BookingDbModel> findByTripAndBookingStatus(TripDbModel trip, String status, Pageable pageable);

    // Price range
    Page<BookingDbModel> findByTotalPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}