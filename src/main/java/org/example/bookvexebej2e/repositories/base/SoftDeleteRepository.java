package org.example.bookvexebej2e.repositories.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SoftDeleteRepository<T, ID> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {

    // Active entities
    List<T> findAllByIsActiveTrue();

    Page<T> findAllByIsActiveTrue(Pageable pageable);

    // Inactive entities
    List<T> findAllByIsActiveFalse();

    Page<T> findAllByIsActiveFalse(Pageable pageable);

    // Count methods
    long countByIsActiveTrue();

    long countByIsActiveFalse();
}
