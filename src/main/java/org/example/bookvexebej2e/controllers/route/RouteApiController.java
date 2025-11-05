package org.example.bookvexebej2e.controllers.route;

import org.example.bookvexebej2e.models.dto.route.RouteQuery;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;
import org.example.bookvexebej2e.services.route.RouteService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API Controller for Chat Service
 * Provides public endpoints for route information
 */
@RestController
@RequestMapping("/api/v1/routes")
public class RouteApiController {

    private final RouteService routeService;

    public RouteApiController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * Count total routes
     * GET /api/v1/routes/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        RouteQuery query = new RouteQuery();
        query.setIsDeleted(false);
        Page<RouteResponse> page = routeService.findAll(query);
        return ResponseEntity.ok(page.getTotalElements());
    }

    /**
     * Get all routes with pagination
     * GET /api/v1/routes?page=0&size=20
     */
    @GetMapping
    public ResponseEntity<Page<RouteResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "startLocation") String sort
    ) {
        RouteQuery query = new RouteQuery();
        query.setPage(page);
        query.setSize(size);
        query.setSortBy(sort);
        query.setIsDeleted(false);
        return ResponseEntity.ok(routeService.findAll(query));
    }

    /**
     * Search routes by criteria
     * POST /api/v1/routes/search
     */
    @PostMapping("/search")
    public ResponseEntity<List<RouteResponse>> search(@RequestBody RouteSearchRequest request) {
        RouteQuery query = new RouteQuery();
        query.setStartLocation(request.getStartLocation());
        query.setEndLocation(request.getEndLocation());
        query.setIsDeleted(request.getIsDeleted() != null ? request.getIsDeleted() : false);
        
        Page<RouteResponse> page = routeService.findAll(query);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Search routes with pagination
     * POST /api/v1/routes/search/page
     */
    @PostMapping("/search/page")
    public ResponseEntity<Page<RouteResponse>> searchWithPage(@RequestBody RouteSearchRequest request) {
        RouteQuery query = new RouteQuery();
        query.setStartLocation(request.getStartLocation());
        query.setEndLocation(request.getEndLocation());
        query.setIsDeleted(request.getIsDeleted() != null ? request.getIsDeleted() : false);
        query.setPage(request.getPage() != null ? request.getPage() : 0);
        query.setSize(request.getSize() != null ? request.getSize() : 20);
        query.setSortBy(request.getSort() != null ? request.getSort() : "startLocation");
        
        return ResponseEntity.ok(routeService.findAll(query));
    }

    /**
     * DTO for route search request
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class RouteSearchRequest {
        private String startLocation;
        private String endLocation;
        private Boolean isDeleted;
        private Integer page;
        private Integer size;
        private String sort;
    }
}
