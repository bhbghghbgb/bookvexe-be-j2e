package org.example.bookvexebej2e.services.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.CustomerMapper;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.db.CustomerTypeDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.customer.CustomerCreate;
import org.example.bookvexebej2e.models.dto.customer.CustomerQuery;
import org.example.bookvexebej2e.models.dto.customer.CustomerResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerSelectResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerUpdate;
import org.example.bookvexebej2e.repositories.customer.CustomerRepository;
import org.example.bookvexebej2e.repositories.customer.CustomerTypeRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CustomerTypeRepository customerTypeRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CustomerResponse> findAll() {
        List<CustomerDbModel> entities = customerRepository.findAllNotDeleted();
        return entities.stream()
                .map(customerMapper::toResponse)
                .toList();
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
        CustomerDbModel entity = customerRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, id));
        return customerMapper.toResponse(entity);
    }

    @Transactional
    @Override
    public CustomerResponse create(CustomerCreate createDto) {
        CustomerDbModel entity = new CustomerDbModel();
        entity.setCode(createDto.getCode());
        entity.setName(createDto.getName());
        entity.setEmail(createDto.getEmail());
        entity.setPhone(createDto.getPhone());
        entity.setDescription(createDto.getDescription());

        if (createDto.getCustomerTypeId() != null) {
            CustomerTypeDbModel customerType = customerTypeRepository.findById(createDto.getCustomerTypeId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(CustomerTypeDbModel.class,
                                    createDto.getCustomerTypeId()));
            entity.setCustomerType(customerType);
        }

        CustomerDbModel savedEntity = customerRepository.save(entity);

        UserDbModel userEntity = new UserDbModel();
        userEntity.setUsername(entity.getPhone());
        userEntity.setPassword(passwordEncoder.encode("123456"));
        userEntity.setIsGoogle(false);
        userEntity.setCustomer(savedEntity);

        userRepository.save(userEntity);

        return customerMapper.toResponse(savedEntity);
    }

    @Override
    public CustomerResponse update(UUID id, CustomerUpdate updateDto) {
        CustomerDbModel entity = customerRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, id));

        entity.setCode(updateDto.getCode());
        entity.setName(updateDto.getName());
        entity.setEmail(updateDto.getEmail());
        entity.setPhone(updateDto.getPhone());
        entity.setDescription(updateDto.getDescription());

        if (updateDto.getCustomerTypeId() != null) {
            CustomerTypeDbModel customerType = customerTypeRepository.findById(updateDto.getCustomerTypeId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(CustomerTypeDbModel.class,
                                    updateDto.getCustomerTypeId()));
            entity.setCustomerType(customerType);
        }

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
                .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, id));
        entity.setIsDeleted(false);
        customerRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        CustomerDbModel entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, id));
        entity.setIsDeleted(true);
        customerRepository.save(entity);
    }

    @Override
    public List<CustomerSelectResponse> findAllForSelect() {
        List<CustomerDbModel> entities = customerRepository.findAllNotDeleted();
        return entities.stream()
                .map(customerMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<CustomerSelectResponse> findAllForSelect(CustomerQuery query) {
        Specification<CustomerDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CustomerDbModel> entities = customerRepository.findAll(spec, pageable);
        return entities.map(customerMapper::toSelectResponse);
    }

    private Specification<CustomerDbModel> buildSpecification(CustomerQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

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
            if (query.getCustomerTypeId() != null) {
                predicates.add(cb.equal(root.get("customerType")
                        .get("id"), query.getCustomerTypeId()));
            }

            if (query.getIsDeleted() != null) {
                predicates.add(cb.equal(root.get("isDeleted"), query.getIsDeleted()));
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
