package org.example.bookvexebej2e.services.route;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.route.RouteUserQuery;
import org.example.bookvexebej2e.models.dto.route.RouteUserResponse;
import org.springframework.data.domain.Page;

public interface RouteUserService {
    List<RouteUserResponse> findAll();

    Page<RouteUserResponse> findAll(RouteUserQuery query);

    RouteUserResponse findById(UUID id);

    List<RouteUserResponse> getAllStartLocations();

    List<RouteUserResponse> getAllEndLocations();

    List<RouteUserResponse> getEndLocationsByStartLocation(String location);
}