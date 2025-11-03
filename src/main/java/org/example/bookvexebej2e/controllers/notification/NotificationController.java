package org.example.bookvexebej2e.controllers.notification;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.notification.NotificationCreate;
import org.example.bookvexebej2e.models.dto.notification.NotificationQuery;
import org.example.bookvexebej2e.models.dto.notification.NotificationResponse;
import org.example.bookvexebej2e.models.dto.notification.NotificationSelectResponse;
import org.example.bookvexebej2e.models.dto.notification.NotificationUpdate;
import org.example.bookvexebej2e.services.notification.NotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/pagination")
    public ResponseEntity<Page<NotificationResponse>> findAll2(@RequestBody NotificationQuery query) {
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

    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        notificationService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        notificationService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<NotificationSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(notificationService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<NotificationSelectResponse>> findAllForSelect(NotificationQuery query) {
        return ResponseEntity.ok(notificationService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<NotificationSelectResponse>> findAllForSelect2(@RequestBody NotificationQuery query) {
        return ResponseEntity.ok(notificationService.findAllForSelect(query));
    }

}
