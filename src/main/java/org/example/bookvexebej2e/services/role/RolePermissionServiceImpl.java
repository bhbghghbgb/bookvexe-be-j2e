package org.example.bookvexebej2e.services.role;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.RolePermissionMapper;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RolePermissionDbModel;
import org.example.bookvexebej2e.models.dto.role.*;
import org.example.bookvexebej2e.repositories.role.RolePermissionRepository;
import org.example.bookvexebej2e.repositories.role.RoleRepository;
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
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public List<RolePermissionResponse> findAll() {
        List<RolePermissionDbModel> entities = rolePermissionRepository.findAllNotDeleted();
        return entities.stream()
            .map(rolePermissionMapper::toResponse)
            .toList();
    }

    @Override
    public Page<RolePermissionResponse> findAll(RolePermissionQuery query) {
        Specification<RolePermissionDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<RolePermissionDbModel> entities = rolePermissionRepository.findAll(spec, pageable);
        return entities.map(rolePermissionMapper::toResponse);
    }

    @Override
    public RolePermissionResponse findById(UUID id) {
        RolePermissionDbModel entity = rolePermissionRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("RolePermission not found with id: " + id));
        return rolePermissionMapper.toResponse(entity);
    }

    @Override
    public RolePermissionResponse create(RolePermissionCreate createDto) {
        RolePermissionDbModel entity = new RolePermissionDbModel();
        entity.setIsCanRead(createDto.getIsCanRead());
        entity.setIsCanCreate(createDto.getIsCanCreate());
        entity.setIsCanUpdate(createDto.getIsCanUpdate());
        entity.setIsCanDelete(createDto.getIsCanDelete());
        entity.setIsCanActivate(createDto.getIsCanActivate());
        entity.setIsCanDeactivate(createDto.getIsCanDeactivate());
        entity.setIsCanImport(createDto.getIsCanImport());
        entity.setIsCanExport(createDto.getIsCanExport());

        RoleDbModel role = roleRepository.findById(createDto.getRoleId())
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + createDto.getRoleId()));
        entity.setRole(role);

        RolePermissionDbModel savedEntity = rolePermissionRepository.save(entity);
        return rolePermissionMapper.toResponse(savedEntity);
    }

    @Override
    public RolePermissionResponse update(UUID id, RolePermissionUpdate updateDto) {
        RolePermissionDbModel entity = rolePermissionRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("RolePermission not found with id: " + id));

        entity.setIsCanRead(updateDto.getIsCanRead());
        entity.setIsCanCreate(updateDto.getIsCanCreate());
        entity.setIsCanUpdate(updateDto.getIsCanUpdate());
        entity.setIsCanDelete(updateDto.getIsCanDelete());
        entity.setIsCanActivate(updateDto.getIsCanActivate());
        entity.setIsCanDeactivate(updateDto.getIsCanDeactivate());
        entity.setIsCanImport(updateDto.getIsCanImport());
        entity.setIsCanExport(updateDto.getIsCanExport());

        if (updateDto.getRoleId() != null) {
            RoleDbModel role = roleRepository.findById(updateDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + updateDto.getRoleId()));
            entity.setRole(role);
        }

        RolePermissionDbModel updatedEntity = rolePermissionRepository.save(entity);
        return rolePermissionMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        rolePermissionRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        RolePermissionDbModel entity = rolePermissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("RolePermission not found with id: " + id));
        entity.setIsDeleted(false);
        rolePermissionRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<RolePermissionSelectResponse> findAllForSelect() {
        List<RolePermissionDbModel> entities = rolePermissionRepository.findAllNotDeleted();
        return entities.stream()
            .map(rolePermissionMapper::toSelectResponse)
            .toList();
    }

    private Specification<RolePermissionDbModel> buildSpecification(RolePermissionQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getRoleId() != null) {
                predicates.add(cb.equal(root.get("role")
                    .get("id"), query.getRoleId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(RolePermissionQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
