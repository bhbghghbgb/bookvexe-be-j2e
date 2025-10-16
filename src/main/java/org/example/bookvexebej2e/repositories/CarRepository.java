package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.repositories.base.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends SoftDeleteRepository<CarDbModel, Integer>, JpaSpecificationExecutor<CarDbModel> {
    // License plate uniqueness check
    Optional<CarDbModel> findByLicensePlate(String licensePlate);
}