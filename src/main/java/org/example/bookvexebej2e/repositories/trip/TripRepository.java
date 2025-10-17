package org.example.bookvexebej2e.repositories.trip;


import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends BaseRepository<TripDbModel> {
}
