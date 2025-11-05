package com.bookvexe.notificationservice.dto.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class RolePermissionResponse extends BasePermissionResponse {
    private UUID id;
    private RoleResponse role;
    private String module;
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

    public RolePermissionResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
