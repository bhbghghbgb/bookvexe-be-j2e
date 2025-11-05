package com.bookvexe.notificationservice.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RolePermissionCreate {
    private UUID roleId;
    private String module;
    private Boolean isCanRead;
    private Boolean isCanCreate;
    private Boolean isCanUpdate;
    private Boolean isCanDelete;
    private Boolean isCanActivate;
    private Boolean isCanDeactivate;
    private Boolean isCanImport;
    private Boolean isCanExport;
}
