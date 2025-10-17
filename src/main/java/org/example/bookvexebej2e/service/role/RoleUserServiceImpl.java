package org.example.bookvexebej2e.service.role;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.RoleUserMapper;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.dto.role.*;
import org.example.bookvexebej2e.repository.role.RoleUserRepository;
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
        RoleUserDbModel entity = roleUserRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("RoleUser not found with id: " + id));
        return roleUserMapper.toResponse(entity);
    }

    @Override
    public RoleUserResponse create(RoleUserCreate createDto) {
        RoleUserDbModel entity = roleUserMapper.toEntity(createDto);
        RoleUserDbModel savedEntity = roleUserRepository.save(entity);
        return roleUserMapper.toResponse(savedEntity);
    }

    @Override
    public RoleUserResponse update(UUID id, RoleUserUpdate updateDto) {
        RoleUserDbModel entity = roleUserRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("RoleUser not found with id: " + id));
        roleUserMapper.updateEntity(updateDto, entity);
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
            predicates.add(cb.equal(root.get("isDeleted"), false));

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
