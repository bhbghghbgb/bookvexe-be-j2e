package org.example.bookvexebej2e.dto.role;

import lombok.Data;
import java.util.UUID;

@Data
public class RolePermissionSelectResponse {
    private UUID id;
    private UUID roleId;
}
