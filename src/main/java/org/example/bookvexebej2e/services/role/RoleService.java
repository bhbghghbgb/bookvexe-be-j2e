package org.example.bookvexebej2e.services.role;

import org.example.bookvexebej2e.models.dto.role.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleResponse> findAll();

    Page<RoleResponse> findAll(RoleQuery query);

    RoleResponse findById(UUID id);

    RoleResponse create(RoleCreate createDto);

    RoleResponse update(UUID id, RoleUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<RoleSelectResponse> findAllForSelect();

    Page<RoleSelectResponse> findAllForSelect(RoleQuery query);
}
