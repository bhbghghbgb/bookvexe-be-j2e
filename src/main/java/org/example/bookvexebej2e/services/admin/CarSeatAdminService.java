package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.requests.CarSeatQueryRequest;
import org.example.bookvexebej2e.repositories.CarSeatRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CarSeatAdminService extends BaseAdminService<CarSeatDbModel, Integer, CarSeatQueryRequest> {

    private final CarSeatRepository carSeatRepository;

    @Override
    protected JpaRepository<CarSeatDbModel, Integer> getRepository() {
        return carSeatRepository;
    }

    @Override
    protected Specification<CarSeatDbModel> buildSpecification(CarSeatQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by car ID
            if (request.getCarId() != null) {
                predicates.add(cb.equal(root.get("car")
                    .get("carId"), request.getCarId()));
            }

            // Filter by seat number (partial match)
            if (StringUtils.hasText(request.getSeatNumber())) {
                predicates.add(cb.like(root.get("seatNumber"), "%" + request.getSeatNumber() + "%"));
            }

            // Filter by active status
            if (request.getIsActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), request.getIsActive()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
