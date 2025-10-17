package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarTypeRepository extends JpaRepository<CarTypeDbModel, String> {
    Optional<CarTypeDbModel> findByTypeName(String typeName);

    List<CarTypeDbModel> findByTypeNameContainingIgnoreCase(String typeName);
}