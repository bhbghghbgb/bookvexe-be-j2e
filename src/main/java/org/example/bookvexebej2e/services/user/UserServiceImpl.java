package org.example.bookvexebej2e.services.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.example.bookvexebej2e.configs.SecurityUtils;
import org.example.bookvexebej2e.exceptions.BadRequestException;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.UserMapper;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.auth.CustomerProfileUpdate;
import org.example.bookvexebej2e.models.dto.user.*;
import org.example.bookvexebej2e.repositories.customer.CustomerRepository;
import org.example.bookvexebej2e.repositories.employee.EmployeeRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;

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
            .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, id));
        return userMapper.toResponse(entity);
    }

    /**
     * Retrieves the authenticated user's details and maps them to a UserResponse DTO.
     *
     * @return UserResponse DTO of the currently authenticated user.
     * @throws ResourceNotFoundException if the user is not found (should not happen after auth).
     */
    public UserResponse getCurrentUser() {
        UserDbModel currentUserEntity = securityUtils.getCurrentUserEntity();

        if (currentUserEntity == null) {
            throw new ResourceNotFoundException(UserDbModel.class, "Authenticated user not found.");
        }

        return userMapper.toResponse(currentUserEntity);
    }

    @Override
    public UserResponse create(UserCreate createDto) {
        UserDbModel entity = new UserDbModel();
        entity.setUsername(createDto.getUsername());
        entity.setPassword(passwordEncoder.encode(createDto.getPassword()));
        entity.setIsGoogle(createDto.getIsGoogle());
        entity.setGoogleAccount(createDto.getGoogleAccount());

        if (createDto.getEmployeeId() != null) {
            EmployeeDbModel employee = employeeRepository.findById(createDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(EmployeeDbModel.class, createDto.getEmployeeId()));
            entity.setEmployee(employee);
        }

        if (createDto.getCustomerId() != null) {
            CustomerDbModel customer = customerRepository.findById(createDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, createDto.getCustomerId()));
            entity.setCustomer(customer);
        }

        UserDbModel savedEntity = userRepository.save(entity);
        return userMapper.toResponse(savedEntity);
    }

    @Override
    public UserResponse update(UUID id, UserUpdate updateDto) {
        UserDbModel entity = userRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, id));

        entity.setUsername(updateDto.getUsername());
        entity.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        entity.setIsGoogle(updateDto.getIsGoogle());
        entity.setGoogleAccount(updateDto.getGoogleAccount());

        if (updateDto.getEmployeeId() != null) {
            EmployeeDbModel employee = employeeRepository.findById(updateDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(EmployeeDbModel.class, updateDto.getEmployeeId()));
            entity.setEmployee(employee);
        } else {
            entity.setEmployee(null);
        }

        if (updateDto.getCustomerId() != null) {
            CustomerDbModel customer = customerRepository.findById(updateDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, updateDto.getCustomerId()));
            entity.setCustomer(customer);
        } else {
            entity.setCustomer(null);
        }

        UserDbModel updatedEntity = userRepository.save(entity);
        return userMapper.toResponse(updatedEntity);
    }

    @Transactional
    public UserResponse updateCustomerProfile(UUID userId, CustomerProfileUpdate updateDto) {
        UserDbModel user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getCustomer() == null) {
            throw new BadRequestException("User is not a customer");
        }

        CustomerDbModel customer = user.getCustomer();

        // Check if phone is being changed and if it's already taken
        if (updateDto.getPhone() != null && !updateDto.getPhone().equals(customer.getPhone())) {
            boolean phoneExists = customerRepository.existsByPhoneAndIsDeletedFalse(updateDto.getPhone());
            if (phoneExists) {
                throw new BadRequestException("Phone number already in use");
            }
            customer.setPhone(updateDto.getPhone());

            // Also update username to phone for login
            user.setUsername(updateDto.getPhone());
        }

        // Update other fields
        if (updateDto.getName() != null) {
            customer.setName(updateDto.getName());
        }

        if (updateDto.getEmail() != null) {
            customer.setEmail(updateDto.getEmail());
        }

        // Update password if provided
        if (updateDto.getPassword() != null && !updateDto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        customerRepository.save(customer);
        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse setupCustomerCredentials(UUID userId, CustomerProfileUpdate setupDto) {
        UserDbModel user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getCustomer() == null) {
            throw new BadRequestException("User is not a customer");
        }

        CustomerDbModel customer = user.getCustomer();

        // Validate that phone is provided (will be used as username)
        if (setupDto.getPhone() == null || setupDto.getPhone().trim().isEmpty()) {
            throw new BadRequestException("Phone number is required");
        }

        // Check if phone is already taken
        boolean phoneExists = customerRepository.existsByPhoneAndIsDeletedFalse(setupDto.getPhone());
        if (phoneExists) {
            throw new BadRequestException("Phone number already in use");
        }

        // Validate that password is provided
        if (setupDto.getPassword() == null || setupDto.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password is required");
        }

        // Update customer information
        customer.setPhone(setupDto.getPhone());
        if (setupDto.getName() != null) {
            customer.setName(setupDto.getName());
        }
        if (setupDto.getEmail() != null) {
            customer.setEmail(setupDto.getEmail());
        }

        // Setup username and password
        user.setUsername(setupDto.getPhone());
        user.setPassword(passwordEncoder.encode(setupDto.getPassword()));

        customerRepository.save(customer);
        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        UserDbModel entity = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, id));
        entity.setIsDeleted(false);
        userRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        UserDbModel entity = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, id));
        entity.setIsDeleted(true);
        userRepository.save(entity);
    }

    @Override
    public List<UserSelectResponse> findAllForSelect() {
        List<UserDbModel> entities = userRepository.findAllNotDeleted();
        return entities.stream()
            .map(userMapper::toSelectResponse)
            .toList();
    }

    @Override
    public Page<UserSelectResponse> findAllForSelect(UserQuery query) {
        Specification<UserDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<UserDbModel> entities = userRepository.findAll(spec, pageable);
        return entities.map(userMapper::toSelectResponse);
    }

    private Specification<UserDbModel> buildSpecification(UserQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Remove the isDeleted filter to show all records including deleted ones

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
