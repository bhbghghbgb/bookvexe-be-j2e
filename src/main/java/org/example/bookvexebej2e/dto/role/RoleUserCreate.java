package org.example.bookvexebej2e.dto.role;

import lombok.Data;
import java.util.UUID;

@Data
public class RoleUserCreate {
    private UUID roleId;
    private UUID userId;
}
