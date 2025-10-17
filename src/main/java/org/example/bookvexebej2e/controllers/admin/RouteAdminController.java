package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.requests.RouteCreateUpdateRequest;
import org.example.bookvexebej2e.models.requests.RouteQueryRequest;
import org.example.bookvexebej2e.services.admin.RouteAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/routes")
@Tag(name = "Route Admin", description = "Route management APIs for administrators")
public class RouteAdminController extends BaseAdminController<RouteDbModel, String, RouteQueryRequest> {

    private final RouteAdminService routeService;

    public RouteAdminController(RouteAdminService routeService) {
        this.routeService = routeService;
    }

    @Override
    protected BaseAdminService<RouteDbModel, String, RouteQueryRequest> getService() {
        return routeService;
    }

    /**
     * Override pagination to accept JSON body instead of form model attributes
     */
    @PostMapping("/pagination")
    public ResponseEntity<Page<RouteDbModel>> pagination(@RequestBody RouteQueryRequest queryRequest) {
        Page<RouteDbModel> result = routeService.findAll(queryRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * Create a new route
     */
    @PostMapping
    public ResponseEntity<RouteDbModel> create(@RequestBody RouteCreateUpdateRequest body) {
        RouteDbModel entity = new RouteDbModel();
        entity.setStartLocation(body.getStartLocation());
        entity.setEndLocation(body.getEndLocation());
        entity.setDistanceKm(body.getDistanceKm());
        entity.setEstimatedDuration(body.getEstimatedDuration());
        RouteDbModel saved = routeService.save(entity);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update an existing route
     */
    @PutMapping("/{id}")
    public ResponseEntity<RouteDbModel> update(@PathVariable String id, @RequestBody RouteCreateUpdateRequest body) {
        return routeService.findById(id)
            .map(existing -> {
                existing.setStartLocation(body.getStartLocation());
                existing.setEndLocation(body.getEndLocation());
                existing.setDistanceKm(body.getDistanceKm());
                existing.setEstimatedDuration(body.getEstimatedDuration());
                RouteDbModel updated = routeService.save(existing);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
