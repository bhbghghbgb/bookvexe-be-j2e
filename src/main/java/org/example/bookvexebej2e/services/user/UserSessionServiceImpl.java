package org.example.bookvexebej2e.services.user;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.UserSessionMapper;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.db.UserSessionDbModel;
import org.example.bookvexebej2e.models.dto.user.*;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.example.bookvexebej2e.repositories.user.UserSessionRepository;
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
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;
    private final UserSessionMapper userSessionMapper;

    @Override
    public List<UserSessionResponse> findAll() {
        List<UserSessionDbModel> entities = userSessionRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(userSessionMapper::toResponse)
            .toList();
    }

    @Override
    public Page<UserSessionResponse> findAll(UserSessionQuery query) {
        Specification<UserSessionDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<UserSessionDbModel> entities = userSessionRepository.findAll(spec, pageable);
        return entities.map(userSessionMapper::toResponse);
    }

    @Override
    public UserSessionResponse findById(UUID id) {
        UserSessionDbModel entity = userSessionRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("UserSession not found with id: " + id));
        return userSessionMapper.toResponse(entity);
    }

    @Override
    public UserSessionResponse create(UserSessionCreate createDto) {
        UserSessionDbModel entity = new UserSessionDbModel();
        entity.setAccessToken(createDto.getAccessToken());
        entity.setExpiresAt(createDto.getExpiresAt());
        entity.setRevoked(createDto.getRevoked());

        UserDbModel user = userRepository.findById(createDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + createDto.getUserId()));
        entity.setUser(user);

        UserSessionDbModel savedEntity = userSessionRepository.save(entity);
        return userSessionMapper.toResponse(savedEntity);
    }

    @Override
    public UserSessionResponse update(UUID id, UserSessionUpdate updateDto) {
        UserSessionDbModel entity = userSessionRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("UserSession not found with id: " + id));

        entity.setAccessToken(updateDto.getAccessToken());
        entity.setExpiresAt(updateDto.getExpiresAt());
        entity.setRevoked(updateDto.getRevoked());

        if (updateDto.getUserId() != null) {
            UserDbModel user = userRepository.findById(updateDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + updateDto.getUserId()));
            entity.setUser(user);
        }

        UserSessionDbModel updatedEntity = userSessionRepository.save(entity);
        return userSessionMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        userSessionRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        UserSessionDbModel entity = userSessionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("UserSession not found with id: " + id));
        entity.setIsDeleted(false);
        userSessionRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<UserSessionSelectResponse> findAllForSelect() {
        List<UserSessionDbModel> entities = userSessionRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(userSessionMapper::toSelectResponse)
            .toList();
    }

    private Specification<UserSessionDbModel> buildSpecification(UserSessionQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (query.getUserId() != null) {
                predicates.add(cb.equal(root.get("user")
                    .get("id"), query.getUserId()));
            }
            if (query.getRevoked() != null) {
                predicates.add(cb.equal(root.get("revoked"), query.getRevoked()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(UserSessionQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
