package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.requests.UserQueryRequest;
import org.example.bookvexebej2e.services.admin.UserAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "User Admin", description = "User management APIs for administrators")
public class UserAdminController extends BaseAdminController<UserDbModel, Integer> {

    private final UserAdminService userService;

    public UserAdminController(UserAdminService userService) {
        this.userService = userService;
    }

    @Override
    protected BaseAdminService<UserDbModel, Integer> getService() {
        return userService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserDbModel>> searchUsers(@ModelAttribute UserQueryRequest queryRequest) {
        Page<UserDbModel> users = userService.findUsersByCriteria(queryRequest);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/with-active-roles")
    public ResponseEntity<List<UserDbModel>> getUsersWithActiveRoles() {
        List<UserDbModel> users = userService.findUsersWithActiveRoles();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<RoleUserDbModel>> getUserRoles(@PathVariable Integer userId) {
        List<RoleUserDbModel> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/active/search")
    public ResponseEntity<Page<UserDbModel>> searchActiveUsersByName(@RequestParam String name,
        @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "20") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDbModel> users = userService.findActiveUsersByName(name, pageable);
        return ResponseEntity.ok(users);
    }
}
