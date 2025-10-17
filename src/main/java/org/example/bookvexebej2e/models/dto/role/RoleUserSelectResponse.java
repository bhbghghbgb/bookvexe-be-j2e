package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleUserSelectResponse {
    private UUID id;
    private UUID roleId;
    private UUID userId;
}
