package org.example.bookvexebej2e.services.user;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.UserMapper;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.user.*;
import org.example.bookvexebej2e.repositories.customer.CustomerRepository;
import org.example.bookvexebej2e.repositories.employee.EmployeeRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> findAll() {
        List<UserDbModel> entities = userRepository.findAllNotDeleted();
        return entities.stream()
            .map(userMapper::toResponse)
            .toList();
    }

    @Override
    public Page<UserResponse> findAll(UserQuery query) {
        Specification<UserDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<UserDbModel> entities = userRepository.findAll(spec, pageable);
        return entities.map(userMapper::toResponse);
    }

    @Override
    public UserResponse findById(UUID id) {
        UserDbModel entity = userRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toResponse(entity);
    }

    @Override
    public UserResponse create(UserCreate createDto) {
        UserDbModel entity = new UserDbModel();
        entity.setUsername(createDto.getUsername());
        entity.setPassword(createDto.getPassword());
        entity.setIsGoogle(createDto.getIsGoogle());
        entity.setGoogleAccount(createDto.getGoogleAccount());

        if (createDto.getEmployeeId() != null) {
            EmployeeDbModel employee = employeeRepository.findById(createDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + createDto.getEmployeeId()));
            entity.setEmployee(employee);
        }

        if (createDto.getCustomerId() != null) {
            CustomerDbModel customer = customerRepository.findById(createDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + createDto.getCustomerId()));
            entity.setCustomer(customer);
        }

        UserDbModel savedEntity = userRepository.save(entity);
        return userMapper.toResponse(savedEntity);
    }

    @Override
    public UserResponse update(UUID id, UserUpdate updateDto) {
        UserDbModel entity = userRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        entity.setUsername(updateDto.getUsername());
        entity.setPassword(updateDto.getPassword());
        entity.setIsGoogle(updateDto.getIsGoogle());
        entity.setGoogleAccount(updateDto.getGoogleAccount());

        if (updateDto.getEmployeeId() != null) {
            EmployeeDbModel employee = employeeRepository.findById(updateDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + updateDto.getEmployeeId()));
            entity.setEmployee(employee);
        } else {
            entity.setEmployee(null);
        }

        if (updateDto.getCustomerId() != null) {
            CustomerDbModel customer = customerRepository.findById(updateDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + updateDto.getCustomerId()));
            entity.setCustomer(customer);
        } else {
            entity.setCustomer(null);
        }

        UserDbModel updatedEntity = userRepository.save(entity);
        return userMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        userRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        UserDbModel entity = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        entity.setIsDeleted(false);
        userRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<UserSelectResponse> findAllForSelect() {
        List<UserDbModel> entities = userRepository.findAllNotDeleted();
        return entities.stream()
            .map(userMapper::toSelectResponse)
            .toList();
    }

    private Specification<UserDbModel> buildSpecification(UserQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getUsername() != null && !query.getUsername()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + query.getUsername()
                    .toLowerCase() + "%"));
            }
            if (query.getEmail() != null && !query.getEmail()
                .isEmpty()) {
                // Search in related customer or employee email
                Predicate customerEmailPredicate = cb.like(cb.lower(root.get("customer")
                    .get("email")), "%" + query.getEmail()
                    .toLowerCase() + "%");
                Predicate employeeEmailPredicate = cb.like(cb.lower(root.get("employee")
                    .get("email")), "%" + query.getEmail()
                    .toLowerCase() + "%");
                predicates.add(cb.or(customerEmailPredicate, employeeEmailPredicate));
            }
            if (query.getIsGoogle() != null) {
                predicates.add(cb.equal(root.get("isGoogle"), query.getIsGoogle()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(UserQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
