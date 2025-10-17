package org.example.bookvexebej2e.service.car;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.dto.car.*;
import org.example.bookvexebej2e.mappers.CarEmployeeMapper;
import org.example.bookvexebej2e.models.db.CarEmployeeDbModel;
import org.example.bookvexebej2e.repository.car.CarEmployeeRepository;
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
    private final CarEmployeeMapper carEmployeeMapper;

    @Override
    public List<CarEmployeeResponse> findAll() {
        List<CarEmployeeDbModel> entities = carEmployeeRepository.findAllByIsDeletedFalse();
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
        CarEmployeeDbModel entity = carEmployeeRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("CarEmployee not found with id: " + id));
        return carEmployeeMapper.toResponse(entity);
    }

    @Override
    public CarEmployeeResponse create(CarEmployeeCreate createDto) {
        CarEmployeeDbModel entity = carEmployeeMapper.toEntity(createDto);
        CarEmployeeDbModel savedEntity = carEmployeeRepository.save(entity);
        return carEmployeeMapper.toResponse(savedEntity);
    }

    @Override
    public CarEmployeeResponse update(UUID id, CarEmployeeUpdate updateDto) {
        CarEmployeeDbModel entity = carEmployeeRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("CarEmployee not found with id: " + id));
        carEmployeeMapper.updateEntity(updateDto, entity);
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
            .orElseThrow(() -> new RuntimeException("CarEmployee not found with id: " + id));
        entity.setIsDeleted(false);
        carEmployeeRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<CarEmployeeSelectResponse> findAllForSelect() {
        List<CarEmployeeDbModel> entities = carEmployeeRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(carEmployeeMapper::toSelectResponse)
            .toList();
    }

    private Specification<CarEmployeeDbModel> buildSpecification(CarEmployeeQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

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
