package org.example.bookvexebej2e.services.employee;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.EmployeeMapper;
import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.employee.*;
import org.example.bookvexebej2e.repositories.employee.EmployeeRepository;
import org.example.bookvexebej2e.repositories.role.RoleRepository;
import org.example.bookvexebej2e.repositories.role.RoleUserRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;
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
        EmployeeDbModel entity = employeeRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(EmployeeDbModel.class, id));
        return employeeMapper.toResponse(entity);
    }

    @Transactional
    @Override
    public EmployeeResponse create(EmployeeCreate createDto) {
        // Create employee
        EmployeeDbModel entity = new EmployeeDbModel();
        entity.setCode(createDto.getCode());
        entity.setName(createDto.getName());
        entity.setEmail(createDto.getEmail());
        entity.setPhone(createDto.getPhone());
        entity.setDescription(createDto.getDescription());

        EmployeeDbModel savedEntity = employeeRepository.save(entity);

        // Create user for employee
        UserDbModel userEntity = new UserDbModel();
        userEntity.setUsername(entity.getCode());
        userEntity.setPassword("123456");
        userEntity.setIsGoogle(false);
        userEntity.setEmployee(savedEntity);

        UserDbModel savedUser = userRepository.save(userEntity);

        // Assign roles to user
        if (createDto.getRoleIds() != null && !createDto.getRoleIds().isEmpty()) {
            assignRolesToUser(savedUser, createDto.getRoleIds());
        }

        return employeeMapper.toResponse(savedEntity);
    }

    @Transactional
    @Override
    public EmployeeResponse update(UUID id, EmployeeUpdate updateDto) {
        EmployeeDbModel entity = employeeRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(EmployeeDbModel.class, id));

        entity.setCode(updateDto.getCode());
        entity.setName(updateDto.getName());
        entity.setEmail(updateDto.getEmail());
        entity.setPhone(updateDto.getPhone());
        entity.setDescription(updateDto.getDescription());

        EmployeeDbModel updatedEntity = employeeRepository.save(entity);

        // Update user roles
        UserDbModel user = userRepository.findByEmployeeId(id)
            .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, id));

        if (updateDto.getRoleIds() != null) {
            updateUserRoles(user, updateDto.getRoleIds());
        }

        return employeeMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        employeeRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        EmployeeDbModel entity = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(EmployeeDbModel.class, id));
        entity.setIsDeleted(false);
        employeeRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        EmployeeDbModel entity = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(EmployeeDbModel.class, id));
        entity.setIsDeleted(true);
        employeeRepository.save(entity);
    }

    @Override
    public List<EmployeeSelectResponse> findAllForSelect() {
        List<EmployeeDbModel> entities = employeeRepository.findAllNotDeleted();
        return entities.stream()
            .map(employeeMapper::toSelectResponse)
            .toList();
    }

    @Override
    public Page<EmployeeSelectResponse> findAllForSelect(EmployeeQuery query) {
        Specification<EmployeeDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<EmployeeDbModel> entities = employeeRepository.findAll(spec, pageable);
        return entities.map(employeeMapper::toSelectResponse);
    }

    private void assignRolesToUser(UserDbModel user, List<UUID> roleIds) {
        List<RoleDbModel> roles = roleRepository.findAllById(roleIds);

        List<RoleUserDbModel> roleUsers = roles.stream()
            .map(role -> {
                RoleUserDbModel roleUser = new RoleUserDbModel();
                roleUser.setUser(user);
                roleUser.setRole(role);
                return roleUser;
            })
            .collect(Collectors.toList());

        roleUserRepository.saveAll(roleUsers);
    }

    private void updateUserRoles(UserDbModel user, List<UUID> newRoleIds) {
        // Remove existing roles
        roleUserRepository.deleteByUserId(user.getId());

        // Assign new roles
        if (!newRoleIds.isEmpty()) {
            assignRolesToUser(user, newRoleIds);
        }
    }

    private Specification<EmployeeDbModel> buildSpecification(EmployeeQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getCode() != null && !query.getCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")),
                    "%" + query.getCode().toLowerCase() + "%"));
            }
            if (query.getName() != null && !query.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + query.getName().toLowerCase() + "%"));
            }
            if (query.getEmail() != null && !query.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")),
                    "%" + query.getEmail().toLowerCase() + "%"));
            }
            if (query.getPhone() != null && !query.getPhone().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("phone")),
                    "%" + query.getPhone().toLowerCase() + "%"));
            }

            if (query.getIsDeleted() != null) {
                predicates.add(cb.equal(root.get("isDeleted"), query.getIsDeleted()));
            }

            // Role filtering
            if (query.getRoleIds() != null && !query.getRoleIds().isEmpty()) {
                Join<EmployeeDbModel, UserDbModel> userJoin = root.join("user");
                Join<UserDbModel, RoleUserDbModel> roleUserJoin = userJoin.join("roleUsers");
                Join<RoleUserDbModel, RoleDbModel> roleJoin = roleUserJoin.join("role");

                Predicate rolePredicate = roleJoin.get("id").in(query.getRoleIds());

                if ("AND".equalsIgnoreCase(query.getRoleFilterMode())) {
                    // For AND mode, we need to ensure all roles are present
                    assert cq != null;
                    cq.distinct(true);
                    cq.groupBy(root.get("id"));
                    cq.having(cb.equal(cb.count(roleJoin.get("id")), query.getRoleIds()
                        .size()));
                } else if ("EXCLUDE".equalsIgnoreCase(query.getRoleFilterMode())) {
                    // Exclude employees with any of these roles
                    rolePredicate = cb.not(rolePredicate);
                }
                // Default is OR mode - employee has at least one of the roles

                predicates.add(rolePredicate);
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