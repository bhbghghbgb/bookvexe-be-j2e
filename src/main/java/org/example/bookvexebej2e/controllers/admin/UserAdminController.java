package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.bookvexebej2e.models.requests.UserQueryRequest;
import org.example.bookvexebej2e.models.responses.UserCreateUpdateDto;
import org.example.bookvexebej2e.models.responses.UserDto;
import org.example.bookvexebej2e.services.admin.UserAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "User Admin", description = "User management APIs for administrators")
public class UserAdminController {

    private final UserAdminService userService;

    public UserAdminController(UserAdminService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all entities (without pagination)")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> result = userService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pagination")
    @Operation(summary = "Search with pagination and filters")
    public ResponseEntity<Page<UserDto>> pagination(@ModelAttribute UserQueryRequest queryRequest) {
        // Use the new DTO-returning service method
        Page<UserDto> result = userService.findAll(queryRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDto> findById(@PathVariable Integer id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateUpdateDto createDto) {
        UserDto createdUser = userService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Integer id,
        @Valid @RequestBody UserCreateUpdateDto updateDto) {
        UserDto updatedUser = userService.update(id, updateDto);
        return ResponseEntity.ok(updatedUser);
    }

//    @GetMapping("/{id}/roles")
//    public ResponseEntity<List<RoleUserDto>> getUserRoles(@PathVariable Integer id) {
//        UserDto user = userService.findById(id)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//        return ResponseEntity.ok(user.getRoles());
//    }
}
