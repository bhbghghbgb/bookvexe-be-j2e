package org.example.bookvexebej2e.controllers.car;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.car.CarSeatCreate;
import org.example.bookvexebej2e.models.dto.car.CarSeatQuery;
import org.example.bookvexebej2e.models.dto.car.CarSeatResponse;
import org.example.bookvexebej2e.models.dto.car.CarSeatSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarSeatUpdate;
import org.example.bookvexebej2e.services.car.CarSeatService;
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
@RequestMapping("/admin/car-seats")
public class CarSeatController {

    private final CarSeatService carSeatService;

    public CarSeatController(CarSeatService carSeatService) {
        this.carSeatService = carSeatService;
    }

    @GetMapping
    public ResponseEntity<List<CarSeatResponse>> findAll() {
        return ResponseEntity.ok(carSeatService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<CarSeatResponse>> findAll(CarSeatQuery query) {
        return ResponseEntity.ok(carSeatService.findAll(query));
    }

    @PostMapping("/pagination")
    public ResponseEntity<Page<CarSeatResponse>> findAll2(@RequestBody CarSeatQuery query) {
        return ResponseEntity.ok(carSeatService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarSeatResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(carSeatService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CarSeatResponse> create(@RequestBody CarSeatCreate createDto) {
        return ResponseEntity.ok(carSeatService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarSeatResponse> update(@PathVariable UUID id, @RequestBody CarSeatUpdate updateDto) {
        return ResponseEntity.ok(carSeatService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        carSeatService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        carSeatService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        carSeatService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CarSeatSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(carSeatService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<CarSeatSelectResponse>> findAllForSelect(CarSeatQuery query) {
        return ResponseEntity.ok(carSeatService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<CarSeatSelectResponse>> findAllForSelect2(@RequestBody CarSeatQuery query) {
        return ResponseEntity.ok(carSeatService.findAllForSelect(query));
    }

}
