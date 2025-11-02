package org.example.bookvexebej2e.repositories.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.example.bookvexebej2e.models.db.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Extend SimpleJpaRepository to handle standard operations,
// and implement your custom interface.
public class BaseRepositoryImpl<T extends BaseModel> extends SimpleJpaRepository<T, UUID> implements BaseRepositoryCustom<T> {

    // Use a delegate or the EntityManager directly
    private final EntityManager entityManager;
    private final Class<T> domainClass; // Needed for Criteria API

    // The required constructor
    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager); // if extending SimpleJpaRepository
        this.entityManager = entityManager;
        this.domainClass = entityInformation.getJavaType();
    }

    // --- Criteria API Helper Method ---
    private Predicate getNotDeletedPredicate(CriteriaBuilder cb, Root<T> root) {
        // Ensure the entity extends BaseModel
        if (BaseModel.class.isAssignableFrom(domainClass)) {
            // WHERE e.isDeleted IS NULL OR e.isDeleted = FALSE
            return cb.or(cb.isNull(root.get("isDeleted")), cb.isFalse(root.get("isDeleted")));
        }
        return cb.conjunction(); // Return always true if not a BaseModel
    }

    // --- Implementation of BaseRepositoryCustom methods ---

    @Override
    public Optional<T> findById(String id) {
        return findById(UUID.fromString(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findByIdAndNotDeleted(UUID id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(domainClass);
        Root<T> root = query.from(domainClass);

        Predicate notDeleted = getNotDeletedPredicate(cb, root);

        // Combine ID check with the soft-delete check
        query.where(cb.and(cb.equal(root.get("id"), id), notDeleted));

        try {
            return Optional.of(entityManager.createQuery(query)
                .getSingleResult());
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAllNotDeleted() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(domainClass);
        Root<T> root = query.from(domainClass);

        query.where(getNotDeletedPredicate(cb, root));

        return entityManager.createQuery(query)
            .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAllNotDeleted(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // 1. Count Query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(domainClass);
        countQuery.select(cb.count(countRoot))
            .where(getNotDeletedPredicate(cb, countRoot));
        Long total = entityManager.createQuery(countQuery)
            .getSingleResult();

        if (total == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // 2. Result Query
        CriteriaQuery<T> resultQuery = cb.createQuery(domainClass);
        Root<T> resultRoot = resultQuery.from(domainClass);
        resultQuery.where(getNotDeletedPredicate(cb, resultRoot));

        // Apply sorting from Pageable
        List<Order> orders = new ArrayList<>();

        // Loop through each sorting property defined in the Pageable object
        for (Sort.Order sortOrder : pageable.getSort()) {
            String property = sortOrder.getProperty();

            // Use the CriteriaBuilder to create the ascending or descending order
            if (sortOrder.getDirection() == Sort.Direction.ASC) {
                orders.add(cb.asc(resultRoot.get(property)));
            } else {
                orders.add(cb.desc(resultRoot.get(property)));
            }
        }

        // Apply the collected Order objects to the CriteriaQuery
        resultQuery.orderBy(orders);

        List<T> result = entityManager.createQuery(resultQuery)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        return new PageImpl<>(result, pageable, total);
    }
}
