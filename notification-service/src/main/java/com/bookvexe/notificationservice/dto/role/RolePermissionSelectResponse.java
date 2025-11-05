package com.bookvexe.notificationservice.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RolePermissionSelectResponse {
    private UUID id;
    private RoleResponse role;
}
