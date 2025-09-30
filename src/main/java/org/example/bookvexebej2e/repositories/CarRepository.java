package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.base.SoftDeleteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends SoftDeleteRepository<CarDbModel, Integer> {

    // Owner-based queries
    Page<CarDbModel> findByOwner(UserDbModel owner, Pageable pageable);

    Page<CarDbModel> findByOwnerUserId(Integer userId, Pageable pageable);

    // Car type queries
    Page<CarDbModel> findByCarType(CarTypeDbModel carType, Pageable pageable);

    Page<CarDbModel> findByCarTypeCarTypeId(Integer carTypeId, Pageable pageable);

    // License plate search
    Optional<CarDbModel> findByLicensePlate(String licensePlate);

    Page<CarDbModel> findByLicensePlateContainingIgnoreCase(String licensePlate, Pageable pageable);

    // Seat count queries
    Page<CarDbModel> findBySeatCountGreaterThanEqual(Integer minSeats, Pageable pageable);

    Page<CarDbModel> findBySeatCountBetween(Integer minSeats, Integer maxSeats, Pageable pageable);

    // Active cars for owner
    Page<CarDbModel> findByOwnerAndIsActiveTrue(UserDbModel owner, Pageable pageable);
}