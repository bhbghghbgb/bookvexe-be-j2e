package org.example.bookvexebej2e.services.role;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.RoleMapper;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.dto.role.*;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponse> findAll() {
        List<RoleDbModel> entities = roleRepository.findAllNotDeleted();
        return entities.stream()
            .map(roleMapper::toResponse)
            .toList();
    }

    @Override
    public Page<RoleResponse> findAll(RoleQuery query) {
        Specification<RoleDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<RoleDbModel> entities = roleRepository.findAll(spec, pageable);
        return entities.map(roleMapper::toResponse);
    }

    @Override
    public RoleResponse findById(UUID id) {
        RoleDbModel entity = roleRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(RoleDbModel.class, id));
        return roleMapper.toResponse(entity);
    }

    @Override
    public RoleResponse create(RoleCreate createDto) {
        RoleDbModel entity = new RoleDbModel();
        entity.setCode(createDto.getCode());
        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());

        RoleDbModel savedEntity = roleRepository.save(entity);
        return roleMapper.toResponse(savedEntity);
    }

    @Override
    public RoleResponse update(UUID id, RoleUpdate updateDto) {
        RoleDbModel entity = roleRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(RoleDbModel.class, id));

        entity.setCode(updateDto.getCode());
        entity.setName(updateDto.getName());
        entity.setDescription(updateDto.getDescription());

        RoleDbModel updatedEntity = roleRepository.save(entity);
        return roleMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        roleRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        RoleDbModel entity = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(RoleDbModel.class, id));
        entity.setIsDeleted(false);
        roleRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<RoleSelectResponse> findAllForSelect() {
        List<RoleDbModel> entities = roleRepository.findAllNotDeleted();
        return entities.stream()
            .map(roleMapper::toSelectResponse)
            .toList();
    }

    @Override
    public Page<RoleSelectResponse> findAllForSelect(RoleQuery query) {
        Specification<RoleDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<RoleDbModel> entities = roleRepository.findAll(spec, pageable);
        return entities.map(roleMapper::toSelectResponse);
    }


    private Specification<RoleDbModel> buildSpecification(RoleQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

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

    private Pageable buildPageable(RoleQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
