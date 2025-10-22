package org.example.bookvexebej2e.controllers.user;

import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.user.*;
import org.example.bookvexebej2e.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.READ)
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/pagination")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.READ)
    public ResponseEntity<Page<UserResponse>> findAll(UserQuery query) {
        return ResponseEntity.ok(userService.findAll(query));
    }

    @PostMapping("/pagination")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.READ)
    public ResponseEntity<Page<UserResponse>> findAll2(@RequestBody UserQuery query) {
        return ResponseEntity.ok(userService.findAll(query));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.READ)
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.CREATE)
    public ResponseEntity<UserResponse> create(@RequestBody UserCreate createDto) {
        return ResponseEntity.ok(userService.create(createDto));
    }

    @PutMapping("/{id}")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.UPDATE)
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserUpdate updateDto) {
        return ResponseEntity.ok(userService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.ACTIVATE)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        userService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.DEACTIVATE)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        userService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.READ)
    public ResponseEntity<List<UserSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(userService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    @RequirePermission(module = ModuleCode.USER, action = PermissionAction.READ)
    public ResponseEntity<Page<UserSelectResponse>> findAllForSelect(UserQuery query) {
        return ResponseEntity.ok(userService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<UserSelectResponse>> findAllForSelect2(@RequestBody UserQuery query) {
        return ResponseEntity.ok(userService.findAllForSelect(query));
    }

}
