package org.example.bookvexebej2e.controller.notification;

import org.example.bookvexebej2e.dto.notification.*;
import org.example.bookvexebej2e.service.notification.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> findAll() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<NotificationResponse>> findAll(NotificationQuery query) {
        return ResponseEntity.ok(notificationService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody NotificationCreate createDto) {
        return ResponseEntity.ok(notificationService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> update(@PathVariable UUID id,
        @RequestBody NotificationUpdate updateDto) {
        return ResponseEntity.ok(notificationService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        notificationService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        notificationService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        notificationService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<NotificationSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(notificationService.findAllForSelect());
    }
}
