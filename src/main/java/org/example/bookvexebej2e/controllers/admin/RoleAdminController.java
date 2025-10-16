package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;
import org.example.bookvexebej2e.models.responses.RoleCreateUpdateDto;
import org.example.bookvexebej2e.services.admin.RoleAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/roles")
@Tag(name = "Role Admin", description = "Role management APIs for administrators")
public class RoleAdminController extends BaseAdminController<RoleDbModel, Integer, BasePageableQueryRequest> {

    private final RoleAdminService roleService;

    public RoleAdminController(RoleAdminService roleService) {
        this.roleService = roleService;
    }

    @Override
    protected BaseAdminService<RoleDbModel, Integer, BasePageableQueryRequest> getService() {
        return roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDbModel> create(@Valid @RequestBody RoleCreateUpdateDto createDto) {
        RoleDbModel createdRole = roleService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createdRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDbModel> update(@PathVariable Integer id,
        @Valid @RequestBody RoleCreateUpdateDto updateDto) {
        RoleDbModel updatedRole = roleService.update(id, updateDto);
        return ResponseEntity.ok(updatedRole);
    }
}
