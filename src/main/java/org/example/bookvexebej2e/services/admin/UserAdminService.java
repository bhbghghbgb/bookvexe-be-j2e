package org.example.bookvexebej2e.services.admin;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.UserMapper;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.requests.UserQueryRequest;
import org.example.bookvexebej2e.models.responses.UserCreateUpdateDto;
import org.example.bookvexebej2e.models.responses.UserDto;
import org.example.bookvexebej2e.repositories.RoleRepository;
import org.example.bookvexebej2e.repositories.RoleUserRepository;
import org.example.bookvexebej2e.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    protected JpaRepository<UserDbModel, Integer> getRepository() {
        return userRepository;
    }

    protected Specification<UserDbModel> buildSpecification(UserQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (StringUtils.hasText(request.getName())) {
                predicates.add(cb.like(cb.lower(root.get("fullName")), "%" + request.getName()
                    .toLowerCase() + "%"));
            }

            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + request.getEmail()
                    .toLowerCase() + "%"));
            }

            if (StringUtils.hasText(request.getPhoneNumber())) {
                predicates.add(cb.like(root.get("phoneNumber"), "%" + request.getPhoneNumber() + "%"));
            }

            if (request.getActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), request.getActive()));
            }

            // Role filtering
            if (request.getIncludeRoles() != null && request.getIncludeRoles().length > 0) {
                Join<UserDbModel, RoleUserDbModel> roleUserJoin = root.join("roles", JoinType.INNER);
                Join<RoleUserDbModel, RoleDbModel> roleJoin = roleUserJoin.join("role");
                predicates.add(roleJoin.get("code")
                    .in((Object[]) request.getIncludeRoles()));
            }

            if (request.getExcludeRoles() != null && request.getExcludeRoles().length > 0) {
                Join<UserDbModel, RoleUserDbModel> roleUserJoin = root.join("roles", JoinType.INNER);
                Join<RoleUserDbModel, RoleDbModel> roleJoin = roleUserJoin.join("role");
                predicates.add(cb.not(roleJoin.get("code")
                    .in((Object[]) request.getExcludeRoles())));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public List<UserDto> findAll() {
        List<UserDbModel> dbModels = getRepository().findAll();
        // Map the DbModels to Dtos
        return dbModels.stream()
            .map(userMapper::toDtoWithRoles)
            .collect(Collectors.toList());
    }

    public Page<UserDto> findAll(UserQueryRequest queryRequest) {
        Pageable pageable = queryRequest.toPageable();
        Specification<UserDbModel> spec = buildSpecification(queryRequest);

        Page<UserDbModel> dbModels;
        if (getRepository() instanceof JpaSpecificationExecutor) {
            @SuppressWarnings("unchecked") JpaSpecificationExecutor<UserDbModel> specExecutor =
                (JpaSpecificationExecutor<UserDbModel>) getRepository();
            dbModels = specExecutor.findAll(spec, pageable);
        } else {
            dbModels = getRepository().findAll(pageable);
        }

        // Map the Page of DbModels to a Page of Dtos
        return dbModels.map(userMapper::toDtoWithRoles);
    }

    public Optional<UserDto> findById(Integer id) {
        return userRepository.findById(id)
            .map(userMapper::toDtoWithRoles); // Map to DTO before returning
    }

    public UserDto create(UserCreateUpdateDto createDto) {
        // Validate unique constraints
        if (userRepository.findByEmail(createDto.getEmail())
            .isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.findByPhoneNumber(createDto.getPhoneNumber())
            .isPresent()) {
            throw new RuntimeException("Phone number already exists");
        }

        UserDbModel user = new UserDbModel();
        user.setFullName(createDto.getFullName());
        user.setEmail(createDto.getEmail());
        user.setPhoneNumber(createDto.getPhoneNumber());
        user.setPasswordHash(passwordEncoder.encode(createDto.getPassword()));
        user.setIsActive(createDto.getIsActive());

        UserDbModel savedUser = userRepository.save(user);

        // Assign roles
        if (createDto.getRoleIds() != null && !createDto.getRoleIds()
            .isEmpty()) {
            assignRolesToUser(savedUser, createDto.getRoleIds());
        }

        return userMapper.toDtoWithRoles(savedUser);
    }

    @Transactional
    public UserDto update(Integer id, UserCreateUpdateDto updateDto) {
        UserDbModel user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate unique constraints for email
        if (!user.getEmail()
            .equals(updateDto.getEmail()) && userRepository.findByEmail(updateDto.getEmail())
            .isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Validate unique constraints for phone number
        if (!user.getPhoneNumber()
            .equals(updateDto.getPhoneNumber()) && userRepository.findByPhoneNumber(updateDto.getPhoneNumber())
            .isPresent()) {
            throw new RuntimeException("Phone number already exists");
        }

        user.setFullName(updateDto.getFullName());
        user.setEmail(updateDto.getEmail());
        user.setPhoneNumber(updateDto.getPhoneNumber());

        if (StringUtils.hasText(updateDto.getPassword())) {
            user.setPasswordHash(passwordEncoder.encode(updateDto.getPassword()));
        }

        if (updateDto.getIsActive() != null) {
            user.setIsActive(updateDto.getIsActive());
        }

        // Update roles
        if (updateDto.getRoleIds() != null) {
            // Remove existing roles
            roleUserRepository.deleteByUser(user);
            // Assign new roles
            assignRolesToUser(user, updateDto.getRoleIds());
        }

        return userMapper.toDtoWithRoles(userRepository.save(user));
    }

    private void assignRolesToUser(UserDbModel user, List<Integer> roleIds) {
        List<RoleDbModel> roles = roleRepository.findAllById(roleIds);

        for (RoleDbModel role : roles) {
            RoleUserDbModel roleUser = new RoleUserDbModel();
            roleUser.setUser(user);
            roleUser.setRole(role);
            roleUser.setIsActive(true);
            roleUserRepository.save(roleUser);
        }
    }

    public void delete(Integer id) {
        UserDbModel user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Soft delete by setting isActive to false
        user.setIsActive(false);
        userRepository.save(user);
    }

    /**
     * Lấy danh sách roles của user
     */
    public List<RoleUserDbModel> getUserRoles(Integer userId) {
        return userRepository.findById(userId)
            .map(roleUserRepository::findByUser)
            .orElse(Collections.emptyList());
    }
}