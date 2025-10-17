package org.example.bookvexebej2e.controller.car;

import org.example.bookvexebej2e.dto.car.*;
import org.example.bookvexebej2e.service.car.CarEmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/car-employees")
public class CarEmployeeController {

    private final CarEmployeeService carEmployeeService;

    public CarEmployeeController(CarEmployeeService carEmployeeService) {
        this.carEmployeeService = carEmployeeService;
    }

    @GetMapping
    public ResponseEntity<List<CarEmployeeResponse>> findAll() {
        return ResponseEntity.ok(carEmployeeService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<CarEmployeeResponse>> findAll(CarEmployeeQuery query) {
        return ResponseEntity.ok(carEmployeeService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarEmployeeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(carEmployeeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CarEmployeeResponse> create(@RequestBody CarEmployeeCreate createDto) {
        return ResponseEntity.ok(carEmployeeService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarEmployeeResponse> update(@PathVariable UUID id, @RequestBody CarEmployeeUpdate updateDto) {
        return ResponseEntity.ok(carEmployeeService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        carEmployeeService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        carEmployeeService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        carEmployeeService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CarEmployeeSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(carEmployeeService.findAllForSelect());
    }
}
