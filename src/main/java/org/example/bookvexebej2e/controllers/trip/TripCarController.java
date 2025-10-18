package org.example.bookvexebej2e.controllers.trip;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.trip.TripCarCreate;
import org.example.bookvexebej2e.models.dto.trip.TripCarQuery;
import org.example.bookvexebej2e.models.dto.trip.TripCarResponse;
import org.example.bookvexebej2e.models.dto.trip.TripCarSelectResponse;
import org.example.bookvexebej2e.models.dto.trip.TripCarUpdate;
import org.example.bookvexebej2e.services.trip.TripCarService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/trip-cars")
public class TripCarController {

    private final TripCarService tripCarService;

    public TripCarController(TripCarService tripCarService) {
        this.tripCarService = tripCarService;
    }

    @GetMapping
    public ResponseEntity<List<TripCarResponse>> findAll() {
        return ResponseEntity.ok(tripCarService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<TripCarResponse>> findAll(TripCarQuery query) {
        return ResponseEntity.ok(tripCarService.findAll(query));
    }

    @PostMapping("/pagination")
    public ResponseEntity<Page<TripCarResponse>> findAll2(@RequestBody TripCarQuery query) {
        return ResponseEntity.ok(tripCarService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripCarResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(tripCarService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TripCarResponse> create(@RequestBody TripCarCreate createDto) {
        return ResponseEntity.ok(tripCarService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripCarResponse> update(@PathVariable UUID id, @RequestBody TripCarUpdate updateDto) {
        return ResponseEntity.ok(tripCarService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tripCarService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        tripCarService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        tripCarService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<TripCarSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(tripCarService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<TripCarSelectResponse>> findAllForSelect(TripCarQuery query) {
        return ResponseEntity.ok(tripCarService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<TripCarSelectResponse>> findAllForSelect2(@RequestBody TripCarQuery query) {
        return ResponseEntity.ok(tripCarService.findAllForSelect(query));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<TripCarResponse>> findByTripId(@PathVariable UUID tripId) {
        return ResponseEntity.ok(tripCarService.findByTripId(tripId));
    }

}
