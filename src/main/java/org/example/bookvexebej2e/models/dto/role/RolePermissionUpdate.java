package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RolePermissionUpdate {
    private UUID roleId;
    private Boolean isCanRead;
    private Boolean isCanCreate;
    private Boolean isCanUpdate;
    private Boolean isCanDelete;
    private Boolean isCanActivate;
    private Boolean isCanDeactivate;
    private Boolean isCanImport;
    private Boolean isCanExport;
}
