package org.example.bookvexebej2e.controllers.car;

import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.car.*;
import org.example.bookvexebej2e.services.car.CarTypeService;
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
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.READ)
    public ResponseEntity<List<CarTypeResponse>> findAll() {
        return ResponseEntity.ok(carTypeService.findAll());
    }

    @GetMapping("/pagination")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.READ)
    public ResponseEntity<Page<CarTypeResponse>> findAll(CarTypeQuery query) {
        return ResponseEntity.ok(carTypeService.findAll(query));
    }

    @PostMapping("/pagination")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.READ)
    public ResponseEntity<Page<CarTypeResponse>> findAll2(@RequestBody CarTypeQuery query) {
        return ResponseEntity.ok(carTypeService.findAll(query));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.READ)
    public ResponseEntity<CarTypeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(carTypeService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.CREATE)
    public ResponseEntity<CarTypeResponse> create(@RequestBody CarTypeCreate createDto) {
        return ResponseEntity.ok(carTypeService.create(createDto));
    }

    @PutMapping("/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.UPDATE)
    public ResponseEntity<CarTypeResponse> update(@PathVariable UUID id, @RequestBody CarTypeUpdate updateDto) {
        return ResponseEntity.ok(carTypeService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        carTypeService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.ACTIVATE)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        carTypeService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    @RequirePermission(module = ModuleCode.CAR, action = PermissionAction.DEACTIVATE)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        carTypeService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CarTypeSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(carTypeService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<CarTypeSelectResponse>> findAllForSelect(CarTypeQuery query) {
        return ResponseEntity.ok(carTypeService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<CarTypeSelectResponse>> findAllForSelect2(@RequestBody CarTypeQuery query) {
        return ResponseEntity.ok(carTypeService.findAllForSelect(query));
    }

}
