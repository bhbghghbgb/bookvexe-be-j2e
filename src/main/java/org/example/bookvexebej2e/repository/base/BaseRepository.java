package org.example.bookvexebej2e.repository.base;

import org.example.bookvexebej2e.models.db.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {

    Optional<T> findByIdAndIsDeletedFalse(UUID id);

    List<T> findAllByIsDeletedFalse();

    Page<T> findAllByIsDeletedFalse(Pageable pageable);

    default void softDelete(T entity) {
        if (entity instanceof BaseModel) {
            ((BaseModel) entity).setIsDeleted(true);
            save(entity);
        }
    }

    default void softDeleteById(UUID id) {
        findById(id).ifPresent(this::softDelete);
    }
}
