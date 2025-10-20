package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RolePermissionResponse {
    private UUID id;
    private RoleResponse role;
    private Boolean isCanRead;
    private Boolean isCanCreate;
    private Boolean isCanUpdate;
    private Boolean isCanDelete;
    private Boolean isCanActivate;
    private Boolean isCanDeactivate;
    private Boolean isCanImport;
    private Boolean isCanExport;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
