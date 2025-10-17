package org.example.bookvexebej2e.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleUserSelectResponse {
    private UUID id;
    private UUID roleId;
    private UUID userId;
}
