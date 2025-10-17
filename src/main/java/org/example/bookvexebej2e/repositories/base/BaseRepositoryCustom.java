package org.example.bookvexebej2e.repositories.base;

import org.example.bookvexebej2e.models.db.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Interface for methods requiring custom implementation (like NULL/FALSE checks)
public interface BaseRepositoryCustom<T extends BaseModel> {

    // Abstract declaration of custom methods
    Optional<T> findByIdAndNotDeleted(UUID id);

    List<T> findAllNotDeleted();

    Page<T> findAllNotDeleted(Pageable pageable);
}
