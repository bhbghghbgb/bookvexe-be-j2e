package org.example.bookvexebej2e.controllers.route;

import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.route.*;
import org.example.bookvexebej2e.services.route.RouteService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.READ)
    public ResponseEntity<List<RouteResponse>> findAll() {
        return ResponseEntity.ok(routeService.findAll());
    }

    @GetMapping("/pagination")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.READ)
    public ResponseEntity<Page<RouteResponse>> findAll(RouteQuery query) {
        return ResponseEntity.ok(routeService.findAll(query));
    }

    @PostMapping("/pagination")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.READ)
    public ResponseEntity<Page<RouteResponse>> findAll2(@RequestBody RouteQuery query) {
        return ResponseEntity.ok(routeService.findAll(query));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.READ)
    public ResponseEntity<RouteResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.CREATE)
    public ResponseEntity<RouteResponse> create(@RequestBody RouteCreate createDto) {
        return ResponseEntity.ok(routeService.create(createDto));
    }

    @PutMapping("/{id}")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.UPDATE)
    public ResponseEntity<RouteResponse> update(@PathVariable UUID id, @RequestBody RouteUpdate updateDto) {
        return ResponseEntity.ok(routeService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        routeService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.ACTIVATE)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        routeService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.DEACTIVATE)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        routeService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.READ)
    public ResponseEntity<List<RouteSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(routeService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    @RequirePermission(module = ModuleCode.ROUTE, action = PermissionAction.READ)
    public ResponseEntity<Page<RouteSelectResponse>> findAllForSelect(RouteQuery query) {
        return ResponseEntity.ok(routeService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<RouteSelectResponse>> findAllForSelect2(@RequestBody RouteQuery query) {
        return ResponseEntity.ok(routeService.findAllForSelect(query));
    }

}
