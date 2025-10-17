package org.example.bookvexebej2e.controller.trip;

import org.example.bookvexebej2e.dto.trip.*;
import org.example.bookvexebej2e.service.trip.TripStopService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/trip-stops")
public class TripStopController {

    private final TripStopService tripStopService;

    public TripStopController(TripStopService tripStopService) {
        this.tripStopService = tripStopService;
    }

    @GetMapping
    public ResponseEntity<List<TripStopResponse>> findAll() {
        return ResponseEntity.ok(tripStopService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<TripStopResponse>> findAll(TripStopQuery query) {
        return ResponseEntity.ok(tripStopService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripStopResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(tripStopService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TripStopResponse> create(@RequestBody TripStopCreate createDto) {
        return ResponseEntity.ok(tripStopService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripStopResponse> update(@PathVariable UUID id, @RequestBody TripStopUpdate updateDto) {
        return ResponseEntity.ok(tripStopService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tripStopService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        tripStopService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        tripStopService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<TripStopSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(tripStopService.findAllForSelect());
    }
}
