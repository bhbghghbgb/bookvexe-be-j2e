package org.example.bookvexebej2e.services.car;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.CarSeatMapper;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.dto.car.*;
import org.example.bookvexebej2e.repositories.car.CarRepository;
import org.example.bookvexebej2e.repositories.car.CarSeatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarSeatServiceImpl implements CarSeatService {

    private final CarSeatRepository carSeatRepository;
    private final CarRepository carRepository;
    private final CarSeatMapper carSeatMapper;

    @Override
    public List<CarSeatResponse> findAll() {
        List<CarSeatDbModel> entities = carSeatRepository.findAllNotDeleted();
        return entities.stream()
            .map(carSeatMapper::toResponse)
            .toList();
    }

    @Override
    public Page<CarSeatResponse> findAll(CarSeatQuery query) {
        Specification<CarSeatDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarSeatDbModel> entities = carSeatRepository.findAll(spec, pageable);
        return entities.map(carSeatMapper::toResponse);
    }

    @Override
    public CarSeatResponse findById(UUID id) {
        CarSeatDbModel entity = carSeatRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("CarSeat not found with id: " + id));
        return carSeatMapper.toResponse(entity);
    }

    @Override
    public CarSeatResponse create(CarSeatCreate createDto) {
        CarSeatDbModel entity = new CarSeatDbModel();
        entity.setSeatNumber(createDto.getSeatNumber());
        entity.setSeatPosition(createDto.getSeatPosition());

        CarDbModel car = carRepository.findById(createDto.getCarId())
            .orElseThrow(() -> new RuntimeException("Car not found with id: " + createDto.getCarId()));
        entity.setCar(car);

        CarSeatDbModel savedEntity = carSeatRepository.save(entity);
        return carSeatMapper.toResponse(savedEntity);
    }

    @Override
    public CarSeatResponse update(UUID id, CarSeatUpdate updateDto) {
        CarSeatDbModel entity = carSeatRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("CarSeat not found with id: " + id));

        entity.setSeatNumber(updateDto.getSeatNumber());
        entity.setSeatPosition(updateDto.getSeatPosition());

        if (updateDto.getCarId() != null) {
            CarDbModel car = carRepository.findById(updateDto.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + updateDto.getCarId()));
            entity.setCar(car);
        }

        CarSeatDbModel updatedEntity = carSeatRepository.save(entity);
        return carSeatMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        carSeatRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        CarSeatDbModel entity = carSeatRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("CarSeat not found with id: " + id));
        entity.setIsDeleted(false);
        carSeatRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<CarSeatSelectResponse> findAllForSelect() {
        List<CarSeatDbModel> entities = carSeatRepository.findAllNotDeleted();
        return entities.stream()
            .map(carSeatMapper::toSelectResponse)
            .toList();
    }

    private Specification<CarSeatDbModel> buildSpecification(CarSeatQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getCarId() != null) {
                predicates.add(cb.equal(root.get("car")
                    .get("id"), query.getCarId()));
            }
            if (query.getSeatNumber() != null && !query.getSeatNumber()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("seatNumber")), "%" + query.getSeatNumber()
                    .toLowerCase() + "%"));
            }
            if (query.getSeatPosition() != null && !query.getSeatPosition()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("seatPosition")), "%" + query.getSeatPosition()
                    .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(CarSeatQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
