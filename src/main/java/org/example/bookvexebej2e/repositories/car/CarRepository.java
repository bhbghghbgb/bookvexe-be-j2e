package org.example.bookvexebej2e.repositories.car;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends BaseRepository<CarDbModel> {
}
