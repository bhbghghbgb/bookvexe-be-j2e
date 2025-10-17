package org.example.bookvexebej2e.controller;

import org.example.bookvexebej2e.dto.trip.*;
import org.example.bookvexebej2e.service.TripService;
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
    public ResponseEntity<List<TripResponse>> findAll() {
        return ResponseEntity.ok(tripService.findAll());
    }
    
    @GetMapping("/pagination")
    public ResponseEntity<Page<TripResponse>> findAll(TripQuery query) {
        return ResponseEntity.ok(tripService.findAll(query));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<TripResponse> create(@RequestBody TripCreate createDto) {
        return ResponseEntity.ok(tripService.create(createDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> update(@PathVariable UUID id, @RequestBody TripUpdate updateDto) {
        return ResponseEntity.ok(tripService.update(id, updateDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tripService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        tripService.activate(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        tripService.deactivate(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/select")
    public ResponseEntity<List<TripSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(tripService.findAllForSelect());
    }
}
