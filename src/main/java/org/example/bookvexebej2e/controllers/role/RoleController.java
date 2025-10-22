package org.example.bookvexebej2e.controllers.role;

import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.role.*;
import org.example.bookvexebej2e.services.role.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.READ)
    public ResponseEntity<List<RoleResponse>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/pagination")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.READ)
    public ResponseEntity<Page<RoleResponse>> findAll(RoleQuery query) {
        return ResponseEntity.ok(roleService.findAll(query));
    }

    @PostMapping("/pagination")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.READ)
    public ResponseEntity<Page<RoleResponse>> findAll2(@RequestBody RoleQuery query) {
        return ResponseEntity.ok(roleService.findAll(query));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.READ)
    public ResponseEntity<RoleResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.CREATE)
    public ResponseEntity<RoleResponse> create(@RequestBody RoleCreate createDto) {
        return ResponseEntity.ok(roleService.create(createDto));
    }

    @PutMapping("/{id}")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.UPDATE)
    public ResponseEntity<RoleResponse> update(@PathVariable UUID id, @RequestBody RoleUpdate updateDto) {
        return ResponseEntity.ok(roleService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        roleService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.ACTIVATE)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        roleService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.DEACTIVATE)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        roleService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.READ)
    public ResponseEntity<List<RoleSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(roleService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    @RequirePermission(module = ModuleCode.ROLE, action = PermissionAction.READ)
    public ResponseEntity<Page<RoleSelectResponse>> findAllForSelect(RoleQuery query) {
        return ResponseEntity.ok(roleService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<RoleSelectResponse>> findAllForSelect2(@RequestBody RoleQuery query) {
        return ResponseEntity.ok(roleService.findAllForSelect(query));
    }

}
