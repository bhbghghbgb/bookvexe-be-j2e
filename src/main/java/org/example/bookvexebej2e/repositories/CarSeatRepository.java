package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarSeatRepository extends JpaRepository<CarSeatDbModel, Integer> {
    List<CarSeatDbModel> findByCar(CarDbModel car);

    List<CarSeatDbModel> findByCarCarId(Integer carId);

    Optional<CarSeatDbModel> findByCarAndSeatNumber(CarDbModel car, String seatNumber);
}
