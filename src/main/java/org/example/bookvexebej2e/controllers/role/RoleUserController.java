package org.example.bookvexebej2e.controllers.role;

import org.example.bookvexebej2e.models.dto.role.*;
import org.example.bookvexebej2e.services.role.RoleUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/role-users")
public class RoleUserController {

    private final RoleUserService roleUserService;

    public RoleUserController(RoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    @GetMapping
    public ResponseEntity<List<RoleUserResponse>> findAll() {
        return ResponseEntity.ok(roleUserService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<RoleUserResponse>> findAll(RoleUserQuery query) {
        return ResponseEntity.ok(roleUserService.findAll(query));
    }

    @PostMapping("/pagination")
    public ResponseEntity<Page<RoleUserResponse>> findAll2(@RequestBody RoleUserQuery query) {
        return ResponseEntity.ok(roleUserService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleUserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(roleUserService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleUserResponse> create(@RequestBody RoleUserCreate createDto) {
        return ResponseEntity.ok(roleUserService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleUserResponse> update(@PathVariable UUID id, @RequestBody RoleUserUpdate updateDto) {
        return ResponseEntity.ok(roleUserService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        roleUserService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        roleUserService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        roleUserService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<RoleUserSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(roleUserService.findAllForSelect());
    }
}
