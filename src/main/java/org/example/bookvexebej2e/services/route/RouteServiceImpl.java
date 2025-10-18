package org.example.bookvexebej2e.services.route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.RouteMapper;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.dto.route.RouteCreate;
import org.example.bookvexebej2e.models.dto.route.RouteQuery;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;
import org.example.bookvexebej2e.models.dto.route.RouteSelectResponse;
import org.example.bookvexebej2e.models.dto.route.RouteUpdate;
import org.example.bookvexebej2e.repositories.route.RouteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Override
    public List<RouteResponse> findAll() {
        List<RouteDbModel> entities = routeRepository.findAllNotDeleted();
        return entities.stream()
                .map(routeMapper::toResponse)
                .toList();
    }

    @Override
    public Page<RouteResponse> findAll(RouteQuery query) {
        Specification<RouteDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<RouteDbModel> entities = routeRepository.findAll(spec, pageable);
        return entities.map(routeMapper::toResponse);
    }

    @Override
    public RouteResponse findById(UUID id) {
        RouteDbModel entity = routeRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, id));
        return routeMapper.toResponse(entity);
    }

    @Override
    public RouteResponse create(RouteCreate createDto) {
        RouteDbModel entity = new RouteDbModel();
        entity.setStartLocation(createDto.getStartLocation());
        entity.setEndLocation(createDto.getEndLocation());
        entity.setDistanceKm(createDto.getDistanceKm());
        entity.setEstimatedDuration(createDto.getEstimatedDuration());

        RouteDbModel savedEntity = routeRepository.save(entity);
        return routeMapper.toResponse(savedEntity);
    }

    @Override
    public RouteResponse update(UUID id, RouteUpdate updateDto) {
        RouteDbModel entity = routeRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, id));

        entity.setStartLocation(updateDto.getStartLocation());
        entity.setEndLocation(updateDto.getEndLocation());
        entity.setDistanceKm(updateDto.getDistanceKm());
        entity.setEstimatedDuration(updateDto.getEstimatedDuration());

        RouteDbModel updatedEntity = routeRepository.save(entity);
        return routeMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        routeRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        RouteDbModel entity = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, id));
        entity.setIsDeleted(false);
        routeRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        RouteDbModel entity = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, id));
        entity.setIsDeleted(true);
        routeRepository.save(entity);
    }

    @Override
    public List<RouteSelectResponse> findAllForSelect() {
        List<RouteDbModel> entities = routeRepository.findAllNotDeleted();
        return entities.stream()
                .map(routeMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<RouteSelectResponse> findAllForSelect(RouteQuery query) {
        Specification<RouteDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<RouteDbModel> entities = routeRepository.findAll(spec, pageable);
        return entities.map(routeMapper::toSelectResponse);
    }

    private Specification<RouteDbModel> buildSpecification(RouteQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Remove the isDeleted filter to show all records including deleted ones

            if (query.getStartLocation() != null && !query.getStartLocation()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("startLocation")), "%" + query.getStartLocation()
                        .toLowerCase() + "%"));
            }
            if (query.getEndLocation() != null && !query.getEndLocation()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("endLocation")), "%" + query.getEndLocation()
                        .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(RouteQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
