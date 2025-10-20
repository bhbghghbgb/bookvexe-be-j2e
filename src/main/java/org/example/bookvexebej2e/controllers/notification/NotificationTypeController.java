package org.example.bookvexebej2e.controllers.notification;

import org.example.bookvexebej2e.models.dto.notification.*;
import org.example.bookvexebej2e.services.notification.NotificationTypeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/notification-types")
public class NotificationTypeController {

    private final NotificationTypeService notificationTypeService;

    public NotificationTypeController(NotificationTypeService notificationTypeService) {
        this.notificationTypeService = notificationTypeService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationTypeResponse>> findAll() {
        return ResponseEntity.ok(notificationTypeService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<NotificationTypeResponse>> findAll(NotificationTypeQuery query) {
        return ResponseEntity.ok(notificationTypeService.findAll(query));
    }

    @PostMapping("/pagination")
    public ResponseEntity<Page<NotificationTypeResponse>> findAll2(@RequestBody NotificationTypeQuery query) {
        return ResponseEntity.ok(notificationTypeService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationTypeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationTypeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NotificationTypeResponse> create(@RequestBody NotificationTypeCreate createDto) {
        return ResponseEntity.ok(notificationTypeService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationTypeResponse> update(@PathVariable UUID id,
        @RequestBody NotificationTypeUpdate updateDto) {
        return ResponseEntity.ok(notificationTypeService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        notificationTypeService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        notificationTypeService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        notificationTypeService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<NotificationTypeSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(notificationTypeService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<NotificationTypeSelectResponse>> findAllForSelect(NotificationTypeQuery query) {
        return ResponseEntity.ok(notificationTypeService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<NotificationTypeSelectResponse>> findAllForSelect2(@RequestBody NotificationTypeQuery query) {
        return ResponseEntity.ok(notificationTypeService.findAllForSelect(query));
    }

}
