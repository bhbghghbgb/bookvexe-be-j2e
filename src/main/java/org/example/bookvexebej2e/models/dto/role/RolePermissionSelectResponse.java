package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RolePermissionSelectResponse {
    private UUID id;
    private RoleResponse role;
}
