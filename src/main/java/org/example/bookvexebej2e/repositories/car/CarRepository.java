package org.example.bookvexebej2e.repositories.car;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends BaseRepository<CarDbModel> {
    Optional<CarDbModel> findByCode(String code);
    Optional<CarDbModel> findByLicensePlate(String licensePlate);
}
