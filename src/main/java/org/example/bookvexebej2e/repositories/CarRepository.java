package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<CarDbModel, String>, JpaSpecificationExecutor<CarDbModel> {
    // License plate uniqueness check
    Optional<CarDbModel> findByLicensePlate(String licensePlate);
}