package org.example.bookvexebej2e.controller.car;

import org.example.bookvexebej2e.dto.car.*;
import org.example.bookvexebej2e.service.car.CarTypeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/car-types")
public class CarTypeController {
    
    private final CarTypeService carTypeService;
    
    public CarTypeController(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }
    
    @GetMapping
    public ResponseEntity<List<CarTypeResponse>> findAll() {
        return ResponseEntity.ok(carTypeService.findAll());
    }
    
    @GetMapping("/pagination")
    public ResponseEntity<Page<CarTypeResponse>> findAll(CarTypeQuery query) {
        return ResponseEntity.ok(carTypeService.findAll(query));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CarTypeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(carTypeService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<CarTypeResponse> create(@RequestBody CarTypeCreate createDto) {
        return ResponseEntity.ok(carTypeService.create(createDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CarTypeResponse> update(@PathVariable UUID id, @RequestBody CarTypeUpdate updateDto) {
        return ResponseEntity.ok(carTypeService.update(id, updateDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        carTypeService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        carTypeService.activate(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        carTypeService.deactivate(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/select")
    public ResponseEntity<List<CarTypeSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(carTypeService.findAllForSelect());
    }
}
