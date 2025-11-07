package org.example.bookvexebej2e.repositories.booking;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends BaseRepository<BookingDbModel> {
    public List<BookingDbModel> findAllNotDeletedWithCustomer();

    /**
     * Retrieves Bookings whose associated Trip departs within the specified time window
     * AND which do NOT already have a 'TYPE_DEPARTURE_REMINDER' notification.
     */
    @Query("""
    SELECT b FROM BookingDbModel b
    JOIN b.trip t
    LEFT JOIN b.notifications n ON n.type.code = :typeCode
    WHERE t.departureTime BETWEEN :startTime AND :endTime
      AND b.bookingStatus != 'PENDING'
      AND n.id IS NULL
    """)
    List<BookingDbModel> findBookingsAwaitingReminder(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("typeCode") String typeCode);
}
