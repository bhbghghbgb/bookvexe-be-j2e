package org.example.bookvexebej2e.service.customer;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.CustomerMapper;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.dto.customer.*;
import org.example.bookvexebej2e.repository.customer.CustomerRepository;
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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerResponse> findAll() {
        List<CustomerDbModel> entities = customerRepository.findAllByIsDeletedFalse();
        return entities.stream().map(customerMapper::toResponse).toList();
    }

    @Override
    public Page<CustomerResponse> findAll(CustomerQuery query) {
        Specification<CustomerDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CustomerDbModel> entities = customerRepository.findAll(spec, pageable);
        return entities.map(customerMapper::toResponse);
    }

    @Override
    public CustomerResponse findById(UUID id) {
        CustomerDbModel entity = customerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return customerMapper.toResponse(entity);
    }

    @Override
    public CustomerResponse create(CustomerCreate createDto) {
        CustomerDbModel entity = customerMapper.toEntity(createDto);
        CustomerDbModel savedEntity = customerRepository.save(entity);
        return customerMapper.toResponse(savedEntity);
    }

    @Override
    public CustomerResponse update(UUID id, CustomerUpdate updateDto) {
        CustomerDbModel entity = customerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerMapper.updateEntity(updateDto, entity);
        CustomerDbModel updatedEntity = customerRepository.save(entity);
        return customerMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        customerRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        CustomerDbModel entity = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        entity.setIsDeleted(false);
        customerRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<CustomerSelectResponse> findAllForSelect() {
        List<CustomerDbModel> entities = customerRepository.findAllByIsDeletedFalse();
        return entities.stream().map(customerMapper::toSelectResponse).toList();
    }

    private Specification<CustomerDbModel> buildSpecification(CustomerQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (query.getCode() != null && !query.getCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + query.getCode().toLowerCase() + "%"));
            }
            if (query.getName() != null && !query.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.getName().toLowerCase() + "%"));
            }
            if (query.getEmail() != null && !query.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + query.getEmail().toLowerCase() + "%"));
            }
            if (query.getPhone() != null && !query.getPhone().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("phone")), "%" + query.getPhone().toLowerCase() + "%"));
            }
            if (query.getCustomerTypeId() != null) {
                predicates.add(cb.equal(root.get("customerType").get("id"), query.getCustomerTypeId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(CustomerQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
