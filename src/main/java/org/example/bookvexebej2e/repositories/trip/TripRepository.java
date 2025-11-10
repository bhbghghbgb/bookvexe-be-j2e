package org.example.bookvexebej2e.repositories.trip;

import java.time.LocalDateTime;
import java.util.List;

import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends BaseRepository<TripDbModel> {
    List<TripDbModel> findByDepartureTimeBetween(LocalDateTime now, LocalDateTime reminderWindow);

    @Query("SELECT t FROM TripDbModel t WHERE t.isDeleted = false AND t.departureTime >= :currentTime ORDER BY t.departureTime ASC")
    List<TripDbModel> findUpcomingTrips(LocalDateTime currentTime);
}
