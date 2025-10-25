package org.example.bookvexebej2e.controllers.permission;
import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.Module;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.role.RolePermissionCreate;
import org.example.bookvexebej2e.services.permission.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/find-all")
    @RequirePermission(module = ModuleCode.PERMISSION, action = PermissionAction.READ)
    public ResponseEntity<List<Module.ModuleDto>> findAll() {
        return ResponseEntity.ok(permissionService.getAllModule());
    }

    @PostMapping("/assign-to-role/{roleId}")
    @RequirePermission(module = ModuleCode.PERMISSION, action = PermissionAction.UPDATE)
    public String assignPermissionToRole(@RequestBody List<RolePermissionCreate> createDtos, @PathVariable UUID roleId) {
        return permissionService.assignPermissionToRole(createDtos, roleId);
    }

    @GetMapping("/by-role/{roleId}")
    @RequirePermission(module = ModuleCode.PERMISSION, action = PermissionAction.READ)
    public ResponseEntity<List<Module.ModuleDto>> getPermissionsByRoleId(@PathVariable UUID roleId) {
        return ResponseEntity.ok(permissionService.getByRole(roleId));
    }
}
