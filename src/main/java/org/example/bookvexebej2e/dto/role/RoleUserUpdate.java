package org.example.bookvexebej2e.dto.role;

import lombok.Data;
import java.util.UUID;

@Data
public class RoleUserUpdate {
    private UUID roleId;
    private UUID userId;
}
