package org.example.bookvexebej2e.services.notification;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.NotificationTypeMapper;
import org.example.bookvexebej2e.models.db.NotificationTypeDbModel;
import org.example.bookvexebej2e.models.dto.notification.*;
import org.example.bookvexebej2e.repositories.notification.NotificationTypeRepository;
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
public class NotificationTypeServiceImpl implements NotificationTypeService {

    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationTypeMapper notificationTypeMapper;

    @Override
    public List<NotificationTypeResponse> findAll() {
        List<NotificationTypeDbModel> entities = notificationTypeRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(notificationTypeMapper::toResponse)
            .toList();
    }

    @Override
    public Page<NotificationTypeResponse> findAll(NotificationTypeQuery query) {
        Specification<NotificationTypeDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<NotificationTypeDbModel> entities = notificationTypeRepository.findAll(spec, pageable);
        return entities.map(notificationTypeMapper::toResponse);
    }

    @Override
    public NotificationTypeResponse findById(UUID id) {
        NotificationTypeDbModel entity = notificationTypeRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("NotificationType not found with id: " + id));
        return notificationTypeMapper.toResponse(entity);
    }

    @Override
    public NotificationTypeResponse create(NotificationTypeCreate createDto) {
        NotificationTypeDbModel entity = new NotificationTypeDbModel();
        entity.setCode(createDto.getCode());
        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());

        NotificationTypeDbModel savedEntity = notificationTypeRepository.save(entity);
        return notificationTypeMapper.toResponse(savedEntity);
    }

    @Override
    public NotificationTypeResponse update(UUID id, NotificationTypeUpdate updateDto) {
        NotificationTypeDbModel entity = notificationTypeRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("NotificationType not found with id: " + id));

        entity.setCode(updateDto.getCode());
        entity.setName(updateDto.getName());
        entity.setDescription(updateDto.getDescription());

        NotificationTypeDbModel updatedEntity = notificationTypeRepository.save(entity);
        return notificationTypeMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        notificationTypeRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        NotificationTypeDbModel entity = notificationTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("NotificationType not found with id: " + id));
        entity.setIsDeleted(false);
        notificationTypeRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<NotificationTypeSelectResponse> findAllForSelect() {
        List<NotificationTypeDbModel> entities = notificationTypeRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(notificationTypeMapper::toSelectResponse)
            .toList();
    }

    private Specification<NotificationTypeDbModel> buildSpecification(NotificationTypeQuery query) {
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

    private Pageable buildPageable(NotificationTypeQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
