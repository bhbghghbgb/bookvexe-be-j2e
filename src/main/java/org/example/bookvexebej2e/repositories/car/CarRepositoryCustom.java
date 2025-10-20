package org.example.bookvexebej2e.repositories.car;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarRepositoryCustom {
    List<CarDbModel> findFuzzy(String searchTerm);

    Page<CarDbModel> findFuzzy(String searchTerm, Pageable pageable);
}
