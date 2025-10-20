package org.example.bookvexebej2e.services.car;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.CarEmployeeMapper;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarEmployeeDbModel;
import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.models.dto.car.*;
import org.example.bookvexebej2e.repositories.car.CarEmployeeRepository;
import org.example.bookvexebej2e.repositories.car.CarRepository;
import org.example.bookvexebej2e.repositories.employee.EmployeeRepository;
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
public class CarEmployeeServiceImpl implements CarEmployeeService {

    private final CarEmployeeRepository carEmployeeRepository;
    private final EmployeeRepository employeeRepository;
    private final CarRepository carRepository;
    private final CarEmployeeMapper carEmployeeMapper;

    @Override
    public List<CarEmployeeResponse> findAll() {
        List<CarEmployeeDbModel> entities = carEmployeeRepository.findAllNotDeleted();
        return entities.stream()
            .map(carEmployeeMapper::toResponse)
            .toList();
    }

    @Override
    public Page<CarEmployeeResponse> findAll(CarEmployeeQuery query) {
        Specification<CarEmployeeDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarEmployeeDbModel> entities = carEmployeeRepository.findAll(spec, pageable);
        return entities.map(carEmployeeMapper::toResponse);
    }

    @Override
    public CarEmployeeResponse findById(UUID id) {
        CarEmployeeDbModel entity = carEmployeeRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(CarEmployeeDbModel.class, id));
        return carEmployeeMapper.toResponse(entity);
    }

    @Override
    public CarEmployeeResponse create(CarEmployeeCreate createDto) {
        CarEmployeeDbModel entity = new CarEmployeeDbModel();

        CarDbModel car = carRepository.findById(createDto.getCarId())
            .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, createDto.getCarId()));
        entity.setCar(car);

        EmployeeDbModel employee = employeeRepository.findById(createDto.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException(EmployeeDbModel.class, createDto.getEmployeeId()));
        entity.setEmployee(employee);

        entity.setRole(createDto.getRole());

        CarEmployeeDbModel savedEntity = carEmployeeRepository.save(entity);
        return carEmployeeMapper.toResponse(savedEntity);
    }

    @Override
    public CarEmployeeResponse update(UUID id, CarEmployeeUpdate updateDto) {
        CarEmployeeDbModel entity = carEmployeeRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(CarEmployeeDbModel.class, id));

        if (updateDto.getCarId() != null) {
            CarDbModel car = carRepository.findById(updateDto.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, updateDto.getCarId()));
            entity.setCar(car);
        }

        if (updateDto.getEmployeeId() != null) {
            EmployeeDbModel employee = employeeRepository.findById(updateDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(EmployeeDbModel.class, updateDto.getEmployeeId()));
            entity.setEmployee(employee);
        }

        if (updateDto.getRole() != null) {
            entity.setRole(updateDto.getRole());
        }

        CarEmployeeDbModel updatedEntity = carEmployeeRepository.save(entity);
        return carEmployeeMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        carEmployeeRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        CarEmployeeDbModel entity = carEmployeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(CarEmployeeDbModel.class, id));
        entity.setIsDeleted(false);
        carEmployeeRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<CarEmployeeSelectResponse> findAllForSelect() {
        List<CarEmployeeDbModel> entities = carEmployeeRepository.findAllNotDeleted();
        return entities.stream()
            .map(carEmployeeMapper::toSelectResponse)
            .toList();
    }

    @Override
    public Page<CarEmployeeSelectResponse> findAllForSelect(CarEmployeeQuery query) {
        Specification<CarEmployeeDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarEmployeeDbModel> entities = carEmployeeRepository.findAll(spec, pageable);
        return entities.map(carEmployeeMapper::toSelectResponse);
    }


    private Specification<CarEmployeeDbModel> buildSpecification(CarEmployeeQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getCarId() != null) {
                predicates.add(cb.equal(root.get("car")
                    .get("id"), query.getCarId()));
            }
            if (query.getEmployeeId() != null) {
                predicates.add(cb.equal(root.get("employee")
                    .get("id"), query.getEmployeeId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(CarEmployeeQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
