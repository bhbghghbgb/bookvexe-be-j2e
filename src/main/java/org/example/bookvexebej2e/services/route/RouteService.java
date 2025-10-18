package org.example.bookvexebej2e.services.route;

import org.example.bookvexebej2e.models.dto.route.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RouteService {
    List<RouteResponse> findAll();

    Page<RouteResponse> findAll(RouteQuery query);

    RouteResponse findById(UUID id);

    RouteResponse create(RouteCreate createDto);

    RouteResponse update(UUID id, RouteUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<RouteSelectResponse> findAllForSelect();

    Page<RouteSelectResponse> findAllForSelect(RouteQuery query);
}
