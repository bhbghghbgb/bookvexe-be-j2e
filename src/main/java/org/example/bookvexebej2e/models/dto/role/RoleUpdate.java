package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;

import java.util.List;

@Data
public class RoleUpdate {
    private String code;
    private String name;
    private String description;

    private List<RolePermissionCreate> rolePermissionUpdates;
}
