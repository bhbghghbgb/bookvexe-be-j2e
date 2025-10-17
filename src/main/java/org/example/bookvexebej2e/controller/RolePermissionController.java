package org.example.bookvexebej2e.controller;

import org.example.bookvexebej2e.dto.role.*;
import org.example.bookvexebej2e.service.RolePermissionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/role-permissions")
public class RolePermissionController {
    
    private final RolePermissionService rolePermissionService;
    
    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }
    
    @GetMapping
    public ResponseEntity<List<RolePermissionResponse>> findAll() {
        return ResponseEntity.ok(rolePermissionService.findAll());
    }
    
    @GetMapping("/pagination")
    public ResponseEntity<Page<RolePermissionResponse>> findAll(RolePermissionQuery query) {
        return ResponseEntity.ok(rolePermissionService.findAll(query));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RolePermissionResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(rolePermissionService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<RolePermissionResponse> create(@RequestBody RolePermissionCreate createDto) {
        return ResponseEntity.ok(rolePermissionService.create(createDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RolePermissionResponse> update(@PathVariable UUID id, @RequestBody RolePermissionUpdate updateDto) {
        return ResponseEntity.ok(rolePermissionService.update(id, updateDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        rolePermissionService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        rolePermissionService.activate(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        rolePermissionService.deactivate(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/select")
    public ResponseEntity<List<RolePermissionSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(rolePermissionService.findAllForSelect());
    }
}
