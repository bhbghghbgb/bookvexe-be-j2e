package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.requests.CarQueryRequest;
import org.example.bookvexebej2e.repositories.CarRepository;
import org.example.bookvexebej2e.repositories.CarSeatRepository;
import org.example.bookvexebej2e.repositories.UserRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarAdminService extends BaseAdminService<CarDbModel, Integer> {

    private final CarRepository carRepository;
    private final CarSeatRepository carSeatRepository;
    private final UserRepository userRepository;

    @Override
    protected JpaRepository<CarDbModel, Integer> getRepository() {
        return carRepository;
    }

    public Page<CarDbModel> findCarsByCriteria(CarQueryRequest queryRequest) {
        Pageable pageable = queryRequest.toPageable();

        if (queryRequest.getOwnerId() != null) {
            if (Boolean.TRUE.equals(queryRequest.getActive())) {
                return userRepository.findById(queryRequest.getOwnerId())
                    .map(owner -> carRepository.findByOwnerAndIsActiveTrue(owner, pageable))
                    .orElse(Page.empty(pageable));
            }
            return carRepository.findByOwnerUserId(queryRequest.getOwnerId(), pageable);
        }

        if (queryRequest.getCarTypeId() != null) {
            return carRepository.findByCarTypeCarTypeId(queryRequest.getCarTypeId(), pageable);
        }

        if (StringUtils.hasText(queryRequest.getLicensePlate())) {
            return carRepository.findByLicensePlateContainingIgnoreCase(queryRequest.getLicensePlate(), pageable);
        }

        if (queryRequest.getMinSeats() != null && queryRequest.getMaxSeats() != null) {
            return carRepository.findBySeatCountBetween(queryRequest.getMinSeats(), queryRequest.getMaxSeats(),
                pageable);
        }

        if (queryRequest.getMinSeats() != null) {
            return carRepository.findBySeatCountGreaterThanEqual(queryRequest.getMinSeats(), pageable);
        }

        if (Boolean.TRUE.equals(queryRequest.getActive())) {
            return carRepository.findAllByIsActiveTrue(pageable);
        }

        return carRepository.findAll(pageable);
    }

    public List<CarSeatDbModel> getCarSeats(Integer carId) {
        return carSeatRepository.findByCarCarId(carId);
    }
}
