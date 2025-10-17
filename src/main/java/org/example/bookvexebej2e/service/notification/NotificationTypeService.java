package org.example.bookvexebej2e.service.notification;

import org.example.bookvexebej2e.dto.notification.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface NotificationTypeService {
    List<NotificationTypeResponse> findAll();
    Page<NotificationTypeResponse> findAll(NotificationTypeQuery query);
    NotificationTypeResponse findById(UUID id);
    NotificationTypeResponse create(NotificationTypeCreate createDto);
    NotificationTypeResponse update(UUID id, NotificationTypeUpdate updateDto);
    void delete(UUID id);
    void activate(UUID id);
    void deactivate(UUID id);
    List<NotificationTypeSelectResponse> findAllForSelect();
}
