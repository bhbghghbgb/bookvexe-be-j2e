package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.requests.CarQueryRequest;
import org.example.bookvexebej2e.repositories.CarRepository;
import org.example.bookvexebej2e.repositories.CarSeatRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarAdminService extends BaseAdminService<CarDbModel, Integer, CarQueryRequest> {

    private final CarRepository carRepository;
    private final CarSeatRepository carSeatRepository;

    @Override
    protected JpaRepository<CarDbModel, Integer> getRepository() {
        return carRepository;
    }

    @Override
    protected Specification<CarDbModel> buildSpecification(CarQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by license plate (partial match, case insensitive)
            if (StringUtils.hasText(request.getLicensePlate())) {
                predicates.add(cb.like(
                    cb.lower(root.get("licensePlate")),
                    "%" + request.getLicensePlate().toLowerCase() + "%"
                ));
            }

            // Filter by owner ID
            if (request.getOwnerId() != null) {
                predicates.add(cb.equal(root.get("owner").get("userId"), request.getOwnerId()));
            }

            // Filter by car type ID
            if (request.getCarTypeId() != null) {
                predicates.add(cb.equal(root.get("carType").get("carTypeId"), request.getCarTypeId()));
            }

            // Filter by minimum seat count
            if (request.getMinSeatCount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("seatCount"), request.getMinSeatCount()));
            }

            // Filter by maximum seat count
            if (request.getMaxSeatCount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("seatCount"), request.getMaxSeatCount()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    /**
     * Get car seats by car ID
     */
    public List<CarSeatDbModel> getCarSeats(Integer carId) {
        return carSeatRepository.findByCarCarId(carId);
    }
}
