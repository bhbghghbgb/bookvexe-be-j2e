package org.example.bookvexebej2e.services.customer;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.CustomerTypeMapper;
import org.example.bookvexebej2e.models.db.CustomerTypeDbModel;
import org.example.bookvexebej2e.models.dto.customer.*;
import org.example.bookvexebej2e.repositories.customer.CustomerTypeRepository;
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
public class CustomerTypeServiceImpl implements CustomerTypeService {

    private final CustomerTypeRepository customerTypeRepository;
    private final CustomerTypeMapper customerTypeMapper;

    @Override
    public List<CustomerTypeResponse> findAll() {
        List<CustomerTypeDbModel> entities = customerTypeRepository.findAllNotDeleted();
        return entities.stream()
            .map(customerTypeMapper::toResponse)
            .toList();
    }

    @Override
    public Page<CustomerTypeResponse> findAll(CustomerTypeQuery query) {
        Specification<CustomerTypeDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CustomerTypeDbModel> entities = customerTypeRepository.findAll(spec, pageable);
        return entities.map(customerTypeMapper::toResponse);
    }

    @Override
    public CustomerTypeResponse findById(UUID id) {
        CustomerTypeDbModel entity = customerTypeRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("CustomerType not found with id: " + id));
        return customerTypeMapper.toResponse(entity);
    }

    @Override
    public CustomerTypeResponse create(CustomerTypeCreate createDto) {
        CustomerTypeDbModel entity = new CustomerTypeDbModel();
        entity.setCode(createDto.getCode());
        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());

        CustomerTypeDbModel savedEntity = customerTypeRepository.save(entity);
        return customerTypeMapper.toResponse(savedEntity);
    }

    @Override
    public CustomerTypeResponse update(UUID id, CustomerTypeUpdate updateDto) {
        CustomerTypeDbModel entity = customerTypeRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("CustomerType not found with id: " + id));

        entity.setCode(updateDto.getCode());
        entity.setName(updateDto.getName());
        entity.setDescription(updateDto.getDescription());

        CustomerTypeDbModel updatedEntity = customerTypeRepository.save(entity);
        return customerTypeMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        customerTypeRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        CustomerTypeDbModel entity = customerTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("CustomerType not found with id: " + id));
        entity.setIsDeleted(false);
        customerTypeRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<CustomerTypeSelectResponse> findAllForSelect() {
        List<CustomerTypeDbModel> entities = customerTypeRepository.findAllNotDeleted();
        return entities.stream()
            .map(customerTypeMapper::toSelectResponse)
            .toList();
    }

    private Specification<CustomerTypeDbModel> buildSpecification(CustomerTypeQuery query) {
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

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(CustomerTypeQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
