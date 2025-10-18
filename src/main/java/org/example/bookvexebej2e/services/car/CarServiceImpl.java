package org.example.bookvexebej2e.services.car;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.CarMapper;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.dto.car.CarCreate;
import org.example.bookvexebej2e.models.dto.car.CarQuery;
import org.example.bookvexebej2e.models.dto.car.CarResponse;
import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarUpdate;
import org.example.bookvexebej2e.repositories.car.CarRepository;
import org.example.bookvexebej2e.repositories.car.CarTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarTypeRepository carTypeRepository;
    private final CarMapper carMapper;
    private final CarSeatService carSeatService;

    @Override
    public List<CarResponse> findAll() {
        List<CarDbModel> entities = carRepository.findAllNotDeleted();
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
        CarDbModel entity = carRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, id));
        return carMapper.toResponse(entity);
    }

    @Override
    public CarResponse create(CarCreate createDto) {
        CarDbModel entity = new CarDbModel();
        entity.setLicensePlate(createDto.getLicensePlate());
        entity.setCode(createDto.getCode());

        CarTypeDbModel carType = carTypeRepository.findById(createDto.getCarTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(CarTypeDbModel.class, createDto.getCarTypeId()));
        entity.setCarType(carType);

        CarDbModel savedEntity = carRepository.save(entity);

        // Automatically create car seats based on car type seat count
        if (carType.getSeatCount() != null && carType.getSeatCount() > 0) {
            carSeatService.createSeatsForCar(savedEntity.getId(), carType.getSeatCount());
        }

        return carMapper.toResponse(savedEntity);
    }

    @Override
    public CarResponse update(UUID id, CarUpdate updateDto) {
        CarDbModel entity = carRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, id));

        entity.setLicensePlate(updateDto.getLicensePlate());
        entity.setCode(updateDto.getCode());

        boolean carTypeChanged = false;
        CarTypeDbModel newCarType = null;

        if (updateDto.getCarTypeId() != null) {
            newCarType = carTypeRepository.findById(updateDto.getCarTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException(CarTypeDbModel.class, updateDto.getCarTypeId()));

            // Check if car type is actually changing
            if (entity.getCarType() == null || !entity.getCarType().getId().equals(updateDto.getCarTypeId())) {
                carTypeChanged = true;
                entity.setCarType(newCarType);
            }
        }

        CarDbModel updatedEntity = carRepository.save(entity);

        // If car type changed, update car seats
        if (carTypeChanged && newCarType != null) {
            // Delete all existing car seats
            carSeatService.deleteAllSeatsByCar(updatedEntity.getId());

            // Create new car seats based on new car type seat count
            if (newCarType.getSeatCount() != null && newCarType.getSeatCount() > 0) {
                carSeatService.createSeatsForCar(updatedEntity.getId(), newCarType.getSeatCount());
            }
        }

        return carMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        carRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        CarDbModel entity = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, id));
        entity.setIsDeleted(false);
        carRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<CarSelectResponse> findAllForSelect() {
        List<CarDbModel> entities = carRepository.findAllNotDeleted();
        return entities.stream()
                .map(carMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<CarSelectResponse> findAllForSelect(CarQuery query) {
        Specification<CarDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarDbModel> entities = carRepository.findAll(spec, pageable);
        return entities.map(carMapper::toSelectResponse);
    }

    private Specification<CarDbModel> buildSpecification(CarQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getCarTypeId() != null) {
                predicates.add(cb.equal(root.get("carType")
                        .get("id"), query.getCarTypeId()));
            }
            if (query.getLicensePlate() != null && !query.getLicensePlate()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("licensePlate")), "%" + query.getLicensePlate()
                        .toLowerCase() + "%"));
            }
            if (query.getCode() != null && !query.getCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + query.getCode()
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
