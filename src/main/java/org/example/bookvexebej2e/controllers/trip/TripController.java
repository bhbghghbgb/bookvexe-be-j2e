package org.example.bookvexebej2e.controllers.trip;

import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.trip.*;
import org.example.bookvexebej2e.services.trip.TripService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.READ)
    public ResponseEntity<List<TripResponse>> findAll() {
        return ResponseEntity.ok(tripService.findAll());
    }

    @GetMapping("/pagination")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.READ)
    public ResponseEntity<Page<TripResponse>> findAll(TripQuery query) {
        return ResponseEntity.ok(tripService.findAll(query));
    }

    @PostMapping("/pagination")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.READ)
    public ResponseEntity<Page<TripResponse>> findAll2(@RequestBody TripQuery query) {
        return ResponseEntity.ok(tripService.findAll(query));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.READ)
    public ResponseEntity<TripResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.CREATE)
    public ResponseEntity<TripResponse> create(@RequestBody TripCreate createDto) {
        return ResponseEntity.ok(tripService.create(createDto));
    }

    @PostMapping("/{id}")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.UPDATE)
    public ResponseEntity<TripResponse> update(@PathVariable UUID id, @RequestBody TripUpdate updateDto) {
        return ResponseEntity.ok(tripService.update(id, updateDto));
    }



    @DeleteMapping("/{id}")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tripService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate/{id}")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.ACTIVATE)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        tripService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate/{id}")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.DEACTIVATE)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        tripService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/select")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.READ)
    public ResponseEntity<List<TripSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(tripService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    @RequirePermission(module = ModuleCode.TRIP, action = PermissionAction.READ)
    public ResponseEntity<Page<TripSelectResponse>> findAllForSelect(TripQuery query) {
        return ResponseEntity.ok(tripService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<TripSelectResponse>> findAllForSelect2(@RequestBody TripQuery query) {
        return ResponseEntity.ok(tripService.findAllForSelect(query));
    }
}