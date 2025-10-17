package org.example.bookvexebej2e.services.car;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.CarMapper;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.dto.car.*;
import org.example.bookvexebej2e.repositories.car.CarRepository;
import org.example.bookvexebej2e.repositories.car.CarTypeRepository;
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
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarTypeRepository carTypeRepository;
    private final CarMapper carMapper;

    @Override
    public List<CarResponse> findAll() {
        List<CarDbModel> entities = carRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(carMapper::toResponse)
            .toList();
    }

    @Override
    public Page<CarResponse> findAll(CarQuery query) {
        Specification<CarDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarDbModel> entities = carRepository.findAll(spec, pageable);
        return entities.map(carMapper::toResponse);
    }

    @Override
    public CarResponse findById(UUID id) {
        CarDbModel entity = carRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
        return carMapper.toResponse(entity);
    }

    @Override
    public CarResponse create(CarCreate createDto) {
        CarDbModel entity = new CarDbModel();
        entity.setLicensePlate(createDto.getLicensePlate());

        CarTypeDbModel carType = carTypeRepository.findById(createDto.getCarTypeId())
            .orElseThrow(() -> new RuntimeException("CarType not found with id: " + createDto.getCarTypeId()));
        entity.setCarType(carType);

        CarDbModel savedEntity = carRepository.save(entity);
        return carMapper.toResponse(savedEntity);
    }

    @Override
    public CarResponse update(UUID id, CarUpdate updateDto) {
        CarDbModel entity = carRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        entity.setLicensePlate(updateDto.getLicensePlate());

        if (updateDto.getCarTypeId() != null) {
            CarTypeDbModel carType = carTypeRepository.findById(updateDto.getCarTypeId())
                .orElseThrow(() -> new RuntimeException("CarType not found with id: " + updateDto.getCarTypeId()));
            entity.setCarType(carType);
        }

        CarDbModel updatedEntity = carRepository.save(entity);
        return carMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        carRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        CarDbModel entity = carRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
        entity.setIsDeleted(false);
        carRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<CarSelectResponse> findAllForSelect() {
        List<CarDbModel> entities = carRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(carMapper::toSelectResponse)
            .toList();
    }

    private Specification<CarDbModel> buildSpecification(CarQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (query.getCarTypeId() != null) {
                predicates.add(cb.equal(root.get("carType")
                    .get("id"), query.getCarTypeId()));
            }
            if (query.getLicensePlate() != null && !query.getLicensePlate()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("licensePlate")), "%" + query.getLicensePlate()
                    .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(CarQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
