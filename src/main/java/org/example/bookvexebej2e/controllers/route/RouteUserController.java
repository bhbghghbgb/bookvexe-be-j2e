package org.example.bookvexebej2e.controllers.route;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.route.RouteUserQuery;
import org.example.bookvexebej2e.models.dto.route.RouteUserResponse;
import org.example.bookvexebej2e.services.route.RouteUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user/routes")
@RequiredArgsConstructor
public class RouteUserController {

    private final RouteUserService routeUserService;

    @PostMapping("/paged")
    public ResponseEntity<Page<RouteUserResponse>> getAllPaged(@RequestBody RouteUserQuery query) {
        Page<RouteUserResponse> routes = routeUserService.findAll(query);
        return ResponseEntity.ok(routes);
    }

    @PostMapping("/all")
    public ResponseEntity<List<RouteUserResponse>> getAll() {
        List<RouteUserResponse> routes = routeUserService.findAll();
        return ResponseEntity.ok(routes);
    }

    @PostMapping("/detail")
    public ResponseEntity<RouteUserResponse> getById(@RequestParam UUID id) {
        RouteUserResponse route = routeUserService.findById(id);
        return ResponseEntity.ok(route);
    }

    @PostMapping("/start-locations")
    public ResponseEntity<List<RouteUserResponse>> getAllStartLocations() {
        List<RouteUserResponse> locations = routeUserService.getAllStartLocations();
        return ResponseEntity.ok(locations);
    }

    @PostMapping("/end-locations")
    public ResponseEntity<List<RouteUserResponse>> getAllEndLocations() {
        List<RouteUserResponse> locations = routeUserService.getAllEndLocations();
        return ResponseEntity.ok(locations);
    }

    @PostMapping("/end-locations/by-start")
    public ResponseEntity<List<RouteUserResponse>> getEndLocationsByStart(@RequestParam String startLocation) {
        List<RouteUserResponse> locations = routeUserService.getEndLocationsByStartLocation(startLocation);
        return ResponseEntity.ok(locations);
    }
}
