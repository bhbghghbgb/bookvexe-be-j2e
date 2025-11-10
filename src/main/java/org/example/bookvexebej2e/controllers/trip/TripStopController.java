package org.example.bookvexebej2e.controllers.trip;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.trip.TripStopCreate;
import org.example.bookvexebej2e.models.dto.trip.TripStopQuery;
import org.example.bookvexebej2e.models.dto.trip.TripStopResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopSelectResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopUpdate;
import org.example.bookvexebej2e.services.trip.TripStopService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/pagination")
    public ResponseEntity<Page<TripStopResponse>> findAll2(@RequestBody TripStopQuery query) {
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> hardDelete(@PathVariable UUID id) {
        tripStopService.hardDelete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        tripStopService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        tripStopService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<TripStopSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(tripStopService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<TripStopSelectResponse>> findAllForSelect(TripStopQuery query) {
        return ResponseEntity.ok(tripStopService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<TripStopSelectResponse>> findAllForSelect2(@RequestBody TripStopQuery query) {
        return ResponseEntity.ok(tripStopService.findAllForSelect(query));
    }

    @GetMapping("/select/{tripId}")
    public ResponseEntity<List<TripStopSelectResponse>> getTripStopsForSelect(@PathVariable UUID tripId) {
        List<TripStopSelectResponse> result = tripStopService.findAllForSelectByTrip(tripId);
        return ResponseEntity.ok(result);
    }

}
