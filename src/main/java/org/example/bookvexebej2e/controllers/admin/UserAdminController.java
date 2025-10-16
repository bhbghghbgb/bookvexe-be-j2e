package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.requests.UserQueryRequest;
import org.example.bookvexebej2e.models.responses.UserCreateUpdateDto;
import org.example.bookvexebej2e.services.admin.UserAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "User Admin", description = "User management APIs for administrators")
public class UserAdminController extends BaseAdminController<UserDbModel, Integer, UserQueryRequest> {

    private final UserAdminService userService;

    public UserAdminController(UserAdminService userService) {
        this.userService = userService;
    }

    @Override
    protected BaseAdminService<UserDbModel, Integer, UserQueryRequest> getService() {
        return userService;
    }

    @PostMapping
    public ResponseEntity<UserDbModel> create(@Valid @RequestBody UserCreateUpdateDto createDto) {
        UserDbModel createdUser = userService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDbModel> update(@PathVariable Integer id,
        @Valid @RequestBody UserCreateUpdateDto updateDto) {
        UserDbModel updatedUser = userService.update(id, updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleUserDbModel>> getUserRoles(@PathVariable Integer id) {
        UserDbModel user = userService.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getRoles());
    }
}
