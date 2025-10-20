package org.example.bookvexebej2e.controllers.user;

import org.example.bookvexebej2e.models.dto.user.*;
import org.example.bookvexebej2e.services.user.UserSessionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/user-sessions")
public class UserSessionController {

    private final UserSessionService userSessionService;

    public UserSessionController(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @GetMapping
    public ResponseEntity<List<UserSessionResponse>> findAll() {
        return ResponseEntity.ok(userSessionService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<UserSessionResponse>> findAll(UserSessionQuery query) {
        return ResponseEntity.ok(userSessionService.findAll(query));
    }

    @PostMapping("/pagination")
    public ResponseEntity<Page<UserSessionResponse>> findAll2(@RequestBody UserSessionQuery query) {
        return ResponseEntity.ok(userSessionService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSessionResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userSessionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserSessionResponse> create(@RequestBody UserSessionCreate createDto) {
        return ResponseEntity.ok(userSessionService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSessionResponse> update(@PathVariable UUID id, @RequestBody UserSessionUpdate updateDto) {
        return ResponseEntity.ok(userSessionService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userSessionService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        userSessionService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        userSessionService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<UserSessionSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(userSessionService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<UserSessionSelectResponse>> findAllForSelect(UserSessionQuery query) {
        return ResponseEntity.ok(userSessionService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<UserSessionSelectResponse>> findAllForSelect2(@RequestBody UserSessionQuery query) {
        return ResponseEntity.ok(userSessionService.findAllForSelect(query));
    }

}
