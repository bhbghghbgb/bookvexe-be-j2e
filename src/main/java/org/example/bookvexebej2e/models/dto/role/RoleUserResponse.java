package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleUserResponse {
    private UUID id;
    private UUID roleId;
    private UUID userId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
