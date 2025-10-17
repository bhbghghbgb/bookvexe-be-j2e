package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.user.UserResponse;

import java.util.UUID;

@Data
public class RoleUserSelectResponse {
    private UUID id;
    private RoleResponse role;
    private UserResponse user;
}
