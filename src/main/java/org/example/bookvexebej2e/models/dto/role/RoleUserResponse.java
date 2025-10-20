package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.user.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleUserResponse {
    private UUID id;
    private RoleResponse role;
    private UserResponse user;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
