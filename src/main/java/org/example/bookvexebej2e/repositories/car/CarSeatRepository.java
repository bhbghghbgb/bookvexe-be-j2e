package org.example.bookvexebej2e.repositories.car;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarSeatRepository extends BaseRepository<CarSeatDbModel> {

    @Query("SELECT cs FROM CarSeatDbModel cs WHERE cs.car.id = :carId")
    List<CarSeatDbModel> findByCarId(@Param("carId") UUID carId);
}
