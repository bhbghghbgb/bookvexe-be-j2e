package org.example.bookvexebej2e.service;

import org.example.bookvexebej2e.dto.role.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RoleUserService {
    List<RoleUserResponse> findAll();
    Page<RoleUserResponse> findAll(RoleUserQuery query);
    RoleUserResponse findById(UUID id);
    RoleUserResponse create(RoleUserCreate createDto);
    RoleUserResponse update(UUID id, RoleUserUpdate updateDto);
    void delete(UUID id);
    void activate(UUID id);
    void deactivate(UUID id);
    List<RoleUserSelectResponse> findAllForSelect();
}
