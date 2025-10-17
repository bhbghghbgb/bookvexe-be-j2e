package org.example.bookvexebej2e.service.role;

import org.example.bookvexebej2e.dto.role.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RolePermissionService {
    List<RolePermissionResponse> findAll();
    Page<RolePermissionResponse> findAll(RolePermissionQuery query);
    RolePermissionResponse findById(UUID id);
    RolePermissionResponse create(RolePermissionCreate createDto);
    RolePermissionResponse update(UUID id, RolePermissionUpdate updateDto);
    void delete(UUID id);
    void activate(UUID id);
    void deactivate(UUID id);
    List<RolePermissionSelectResponse> findAllForSelect();
}
