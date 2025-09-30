package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDbModel, Integer> {

    // User-based notifications
    Page<NotificationDbModel> findByUser(UserDbModel user, Pageable pageable);

    Page<NotificationDbModel> findByUserUserId(Integer userId, Pageable pageable);

    // Booking-based notifications
    Page<NotificationDbModel> findByBooking(BookingDbModel booking, Pageable pageable);

    Page<NotificationDbModel> findByBookingBookingId(Integer bookingId, Pageable pageable);

    // Trip-based notifications
    Page<NotificationDbModel> findByTrip(TripDbModel trip, Pageable pageable);

    // Status and channel queries
    Page<NotificationDbModel> findByIsSent(Boolean isSent, Pageable pageable);

    Page<NotificationDbModel> findByChannel(String channel, Pageable pageable);

    // Type-based queries
    Page<NotificationDbModel> findByType(NotificationTypeDbModel type, Pageable pageable);

    // Date range queries
    Page<NotificationDbModel> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<NotificationDbModel> findBySentAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Unsent notifications
    Page<NotificationDbModel> findByIsSentFalse(Pageable pageable);

    Page<NotificationDbModel> findByIsSentFalseAndCreatedAtBefore(LocalDateTime before, Pageable pageable);
}
