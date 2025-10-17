package org.example.bookvexebej2e.repository;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends BaseRepository<CarDbModel> {
    @Repository
    interface CarTypeRepository extends BaseRepository<CarTypeDbModel> {
    }
}
