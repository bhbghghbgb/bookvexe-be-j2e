package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.requests.CarTypeQueryRequest;
import org.example.bookvexebej2e.repositories.CarTypeRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CarTypeAdminService extends BaseAdminService<CarTypeDbModel, String, CarTypeQueryRequest> {

    private final CarTypeRepository carTypeRepository;

    @Override
    protected JpaRepository<CarTypeDbModel, String> getRepository() {
        return carTypeRepository;
    }

    @Override
    protected Specification<CarTypeDbModel> buildSpecification(CarTypeQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by type name (partial match, case insensitive)
            if (StringUtils.hasText(request.getTypeName())) {
                predicates.add(cb.like(
                    cb.lower(root.get("typeName")),
                    "%" + request.getTypeName().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
