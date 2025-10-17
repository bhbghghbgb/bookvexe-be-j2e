package org.example.bookvexebej2e.controllers.car;

import org.example.bookvexebej2e.models.dto.car.*;
import org.example.bookvexebej2e.services.car.CarService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> findAll() {
        return ResponseEntity.ok(carService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<CarResponse>> findAll(CarQuery query) {
        return ResponseEntity.ok(carService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CarResponse> create(@RequestBody CarCreate createDto) {
        return ResponseEntity.ok(carService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> update(@PathVariable UUID id, @RequestBody CarUpdate updateDto) {
        return ResponseEntity.ok(carService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        carService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        carService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        carService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CarSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(carService.findAllForSelect());
    }
}
