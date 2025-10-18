package org.example.bookvexebej2e.repositories.trip;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TripCarRepository extends BaseRepository<TripCarDbModel> {

    @Query("SELECT tc FROM TripCarDbModel tc WHERE tc.trip.id = :tripId AND (tc.isDeleted = false OR tc.isDeleted IS NULL)")
    List<TripCarDbModel> findByTripIdAndNotDeleted(@Param("tripId") UUID tripId);
}
