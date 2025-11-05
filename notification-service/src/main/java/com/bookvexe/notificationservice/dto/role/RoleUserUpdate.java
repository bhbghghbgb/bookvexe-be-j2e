package com.bookvexe.notificationservice.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleUserUpdate {
    private UUID roleId;
    private UUID userId;
}
