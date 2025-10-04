package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.requests.UserQueryRequest;
import org.example.bookvexebej2e.services.admin.UserAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
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

    /**
     * Lấy danh sách roles của user
     */
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<RoleUserDbModel>> getUserRoles(@PathVariable Integer userId) {
        List<RoleUserDbModel> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }
}
