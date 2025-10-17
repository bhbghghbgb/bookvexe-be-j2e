package org.example.bookvexebej2e.service.employee;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.EmployeeMapper;
import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.models.dto.employee.*;
import org.example.bookvexebej2e.repository.employee.EmployeeRepository;
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
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeResponse> findAll() {
        List<EmployeeDbModel> entities = employeeRepository.findAllNotDeleted();
        return entities.stream()
            .map(employeeMapper::toResponse)
            .toList();
    }

    @Override
    public Page<EmployeeResponse> findAll(EmployeeQuery query) {
        Specification<EmployeeDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<EmployeeDbModel> entities = employeeRepository.findAll(spec, pageable);
        return entities.map(employeeMapper::toResponse);
    }

    @Override
    public EmployeeResponse findById(UUID id) {
        EmployeeDbModel entity = employeeRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return employeeMapper.toResponse(entity);
    }

    @Override
    public EmployeeResponse create(EmployeeCreate createDto) {
        EmployeeDbModel entity = employeeMapper.toEntity(createDto);
        EmployeeDbModel savedEntity = employeeRepository.save(entity);
        return employeeMapper.toResponse(savedEntity);
    }

    @Override
    public EmployeeResponse update(UUID id, EmployeeUpdate updateDto) {
        EmployeeDbModel entity = employeeRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeMapper.updateEntity(updateDto, entity);
        EmployeeDbModel updatedEntity = employeeRepository.save(entity);
        return employeeMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        employeeRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        EmployeeDbModel entity = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        entity.setIsDeleted(false);
        employeeRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<EmployeeSelectResponse> findAllForSelect() {
        List<EmployeeDbModel> entities = employeeRepository.findAllNotDeleted();
        return entities.stream()
            .map(employeeMapper::toSelectResponse)
            .toList();
    }

    private Specification<EmployeeDbModel> buildSpecification(EmployeeQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (query.getCode() != null && !query.getCode()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + query.getCode()
                    .toLowerCase() + "%"));
            }
            if (query.getName() != null && !query.getName()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.getName()
                    .toLowerCase() + "%"));
            }
            if (query.getEmail() != null && !query.getEmail()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + query.getEmail()
                    .toLowerCase() + "%"));
            }
            if (query.getPhone() != null && !query.getPhone()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("phone")), "%" + query.getPhone()
                    .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(EmployeeQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
