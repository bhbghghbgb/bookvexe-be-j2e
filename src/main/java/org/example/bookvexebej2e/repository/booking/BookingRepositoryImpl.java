package org.example.bookvexebej2e.repository.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<BookingDbModel> findFuzzy(String searchTerm) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookingDbModel> query = cb.createQuery(BookingDbModel.class);
        Root<BookingDbModel> root = query.from(BookingDbModel.class);

        Predicate predicate = buildFuzzyPredicate(cb, root, searchTerm);
        query.where(predicate)
            .orderBy(cb.desc(root.get("createdDate")));

        return entityManager.createQuery(query)
            .getResultList();
    }

    @Override
    public Page<BookingDbModel> findFuzzy(String searchTerm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<BookingDbModel> countRoot = countQuery.from(BookingDbModel.class);
        countQuery.select(cb.count(countRoot))
            .where(buildFuzzyPredicate(cb, countRoot, searchTerm));
        Long total = entityManager.createQuery(countQuery)
            .getSingleResult();

        // Result query
        CriteriaQuery<BookingDbModel> resultQuery = cb.createQuery(BookingDbModel.class);
        Root<BookingDbModel> resultRoot = resultQuery.from(BookingDbModel.class);
        resultQuery.where(buildFuzzyPredicate(cb, resultRoot, searchTerm))
            .orderBy(cb.desc(resultRoot.get("createdDate")));

        List<BookingDbModel> result = entityManager.createQuery(resultQuery)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        return new PageImpl<>(result, pageable, total);
    }

    private Predicate buildFuzzyPredicate(CriteriaBuilder cb, Root<BookingDbModel> root, String searchTerm) {
        if (searchTerm == null || searchTerm.trim()
            .isEmpty()) {
            return cb.conjunction();
        }

        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        List<Predicate> predicates = new ArrayList<>();

        // Search in string fields
        predicates.add(cb.like(cb.lower(root.get("code")), likePattern));
        predicates.add(cb.like(cb.lower(root.get("type")), likePattern));
        predicates.add(cb.like(cb.lower(root.get("bookingStatus")), likePattern));

        // Search in numeric fields converted to string
        predicates.add(cb.like(cb.lower(root.get("totalPrice")
            .as(String.class)), likePattern));

        // Search in related entities
        Join<BookingDbModel, Object> userJoin = root.join("user", JoinType.LEFT);
        predicates.add(cb.like(cb.lower(userJoin.get("username")), likePattern));

        Join<BookingDbModel, Object> tripJoin = root.join("trip", JoinType.LEFT);
        predicates.add(cb.like(cb.lower(tripJoin.get("route")
            .get("startLocation")), likePattern));
        predicates.add(cb.like(cb.lower(tripJoin.get("route")
            .get("endLocation")), likePattern));

        return cb.or(predicates.toArray(new Predicate[0]));
    }
}
