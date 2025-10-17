package org.example.bookvexebej2e.controller.route;

import org.example.bookvexebej2e.dto.route.*;
import org.example.bookvexebej2e.service.route.RouteService;
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
    public ResponseEntity<List<RouteResponse>> findAll() {
        return ResponseEntity.ok(routeService.findAll());
    }
    
    @GetMapping("/pagination")
    public ResponseEntity<Page<RouteResponse>> findAll(RouteQuery query) {
        return ResponseEntity.ok(routeService.findAll(query));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(routeService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<RouteResponse> create(@RequestBody RouteCreate createDto) {
        return ResponseEntity.ok(routeService.create(createDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> update(@PathVariable UUID id, @RequestBody RouteUpdate updateDto) {
        return ResponseEntity.ok(routeService.update(id, updateDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        routeService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        routeService.activate(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        routeService.deactivate(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/select")
    public ResponseEntity<List<RouteSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(routeService.findAllForSelect());
    }
}
