package org.example.bookvexebej2e.services.role;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.RoleUserMapper;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.role.*;
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

@Service
@RequiredArgsConstructor
public class RoleUserServiceImpl implements RoleUserService {

    private final RoleUserRepository roleUserRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleUserMapper roleUserMapper;

    @Override
    public List<RoleUserResponse> findAll() {
        List<RoleUserDbModel> entities = roleUserRepository.findAllNotDeleted();
        return entities.stream()
            .map(roleUserMapper::toResponse)
            .toList();
    }

    @Override
    public Page<RoleUserResponse> findAll(RoleUserQuery query) {
        Specification<RoleUserDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<RoleUserDbModel> entities = roleUserRepository.findAll(spec, pageable);
        return entities.map(roleUserMapper::toResponse);
    }

    @Override
    public RoleUserResponse findById(UUID id) {
        RoleUserDbModel entity = roleUserRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("RoleUser not found with id: " + id));
        return roleUserMapper.toResponse(entity);
    }

    @Override
    public RoleUserResponse create(RoleUserCreate createDto) {
        RoleUserDbModel entity = new RoleUserDbModel();

        RoleDbModel role = roleRepository.findById(createDto.getRoleId())
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + createDto.getRoleId()));
        entity.setRole(role);

        UserDbModel user = userRepository.findById(createDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + createDto.getUserId()));
        entity.setUser(user);

        RoleUserDbModel savedEntity = roleUserRepository.save(entity);
        return roleUserMapper.toResponse(savedEntity);
    }

    @Override
    public RoleUserResponse update(UUID id, RoleUserUpdate updateDto) {
        RoleUserDbModel entity = roleUserRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("RoleUser not found with id: " + id));

        if (updateDto.getRoleId() != null) {
            RoleDbModel role = roleRepository.findById(updateDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + updateDto.getRoleId()));
            entity.setRole(role);
        }

        if (updateDto.getUserId() != null) {
            UserDbModel user = userRepository.findById(updateDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + updateDto.getUserId()));
            entity.setUser(user);
        }

        RoleUserDbModel updatedEntity = roleUserRepository.save(entity);
        return roleUserMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        roleUserRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        RoleUserDbModel entity = roleUserRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("RoleUser not found with id: " + id));
        entity.setIsDeleted(false);
        roleUserRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<RoleUserSelectResponse> findAllForSelect() {
        List<RoleUserDbModel> entities = roleUserRepository.findAllNotDeleted();
        return entities.stream()
            .map(roleUserMapper::toSelectResponse)
            .toList();
    }

    private Specification<RoleUserDbModel> buildSpecification(RoleUserQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getRoleId() != null) {
                predicates.add(cb.equal(root.get("role")
                    .get("id"), query.getRoleId()));
            }
            if (query.getUserId() != null) {
                predicates.add(cb.equal(root.get("user")
                    .get("id"), query.getUserId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(RoleUserQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
