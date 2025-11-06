package org.example.bookvexebej2e.repositories.trip;

import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends BaseRepository<TripDbModel> {
    List<TripDbModel> findByDepartureTimeBetween(LocalDateTime now, LocalDateTime reminderWindow);
}
