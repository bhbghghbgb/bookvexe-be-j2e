package org.example.bookvexebej2e.repositories.car;

import java.util.ArrayList;
import java.util.List;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<CarDbModel> findFuzzy(String searchTerm) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CarDbModel> query = cb.createQuery(CarDbModel.class);
        Root<CarDbModel> root = query.from(CarDbModel.class);

        Predicate predicate = buildFuzzyPredicate(cb, root, searchTerm);
        query.where(predicate)
                .orderBy(cb.desc(root.get("createdDate")));

        return entityManager.createQuery(query)
                .getResultList();
    }

    @Override
    public Page<CarDbModel> findFuzzy(String searchTerm, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CarDbModel> countRoot = countQuery.from(CarDbModel.class);
        countQuery.select(cb.count(countRoot))
                .where(buildFuzzyPredicate(cb, countRoot, searchTerm));
        Long total = entityManager.createQuery(countQuery)
                .getSingleResult();

        CriteriaQuery<CarDbModel> resultQuery = cb.createQuery(CarDbModel.class);
        Root<CarDbModel> resultRoot = resultQuery.from(CarDbModel.class);
        resultQuery.where(buildFuzzyPredicate(cb, resultRoot, searchTerm))
                .orderBy(cb.desc(resultRoot.get("createdDate")));

        List<CarDbModel> result = entityManager.createQuery(resultQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(result, pageable, total);
    }

    private Predicate buildFuzzyPredicate(CriteriaBuilder cb, Root<CarDbModel> root, String searchTerm) {
        if (searchTerm == null || searchTerm.trim()
                .isEmpty()) {
            return cb.conjunction();
        }

        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.like(cb.lower(root.get("licensePlate")), likePattern));
        predicates.add(cb.like(cb.lower(root.get("code")), likePattern));
        Join<CarDbModel, Object> carTypeJoin = root.join("carType", JoinType.LEFT);
        predicates.add(cb.like(cb.lower(carTypeJoin.get("code")), likePattern));
        predicates.add(cb.like(cb.lower(carTypeJoin.get("name")), likePattern));
        predicates.add(cb.like(cb.lower(carTypeJoin.get("description")), likePattern));

        return cb.or(predicates.toArray(new Predicate[0]));
    }
}
