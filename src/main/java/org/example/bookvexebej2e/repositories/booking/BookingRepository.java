package org.example.bookvexebej2e.repositories.booking;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends BaseRepository<BookingDbModel> {
    public List<BookingDbModel> findAllNotDeletedWithCustomer();

    /**
     * Retrieves Bookings whose associated Trip departs within the specified time
     * window
     * AND which do NOT already have a 'TYPE_DEPARTURE_REMINDER' notification.
     */
    @Query("""
            SELECT b FROM BookingDbModel b
            JOIN b.trip t
            WHERE t.departureTime BETWEEN :startTime AND :endTime
              AND b.bookingStatus = :status
              AND NOT EXISTS (
                    SELECT 1
                    FROM NotificationDbModel n
                    WHERE n.booking = b
                      AND n.type.code = :typeCode
              )
        """)
    List<BookingDbModel> findBookingsAwaitingReminder(@Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime, @Param("typeCode") String typeCode, @Param("status") String status);

    Optional<BookingDbModel> findByCode(String code);
}
