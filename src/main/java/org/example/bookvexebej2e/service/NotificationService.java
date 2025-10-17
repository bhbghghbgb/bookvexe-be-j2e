package org.example.bookvexebej2e.service;

import org.example.bookvexebej2e.dto.notification.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationResponse> findAll();
    Page<NotificationResponse> findAll(NotificationQuery query);
    NotificationResponse findById(UUID id);
    NotificationResponse create(NotificationCreate createDto);
    NotificationResponse update(UUID id, NotificationUpdate updateDto);
    void delete(UUID id);
    void activate(UUID id);
    void deactivate(UUID id);
    List<NotificationSelectResponse> findAllForSelect();
}
