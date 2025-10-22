package org.example.bookvexebej2e.controllers.car;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.car.CarCreate;
import org.example.bookvexebej2e.models.dto.car.CarQuery;
import org.example.bookvexebej2e.models.dto.car.CarResponse;
import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarUpdate;
import org.example.bookvexebej2e.services.car.CarService;
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
@RequestMapping("/admin/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.READ)
    public ResponseEntity<List<CarResponse>> findAll() {
        return ResponseEntity.ok(carService.findAll());
    }

    @GetMapping("/pagination")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.READ)
    public ResponseEntity<Page<CarResponse>> findAll(CarQuery query) {
        return ResponseEntity.ok(carService.findAll(query));
    }

    @PostMapping("/pagination")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.READ)
    public ResponseEntity<Page<CarResponse>> findAll2(@RequestBody CarQuery query) {
        return ResponseEntity.ok(carService.findAll(query));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.READ)
    public ResponseEntity<CarResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.CREATE)
    public ResponseEntity<CarResponse> create(@RequestBody CarCreate createDto) {
        return ResponseEntity.ok(carService.create(createDto));
    }

    @PutMapping("/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.UPDATE)
    public ResponseEntity<CarResponse> update(@PathVariable UUID id, @RequestBody CarUpdate updateDto) {
        return ResponseEntity.ok(carService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        carService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.ACTIVATE)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        carService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.DEACTIVATE)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        carService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CarSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(carService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<CarSelectResponse>> findAllForSelect(CarQuery query) {
        return ResponseEntity.ok(carService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<CarSelectResponse>> findAllForSelect2(@RequestBody CarQuery query) {
        return ResponseEntity.ok(carService.findAllForSelect(query));
    }

}
