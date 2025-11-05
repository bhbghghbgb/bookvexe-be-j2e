package com.bookvexe.notificationservice.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin quyền của user trên một module cụ thể
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModulePermission {

    private String moduleCode;
    private String moduleName;
    private String description;
    private String urlPageView;

    private Boolean canRead;
    private Boolean canCreate;
    private Boolean canUpdate;
    private Boolean canDelete;
    private Boolean canActivate;
    private Boolean canDeactivate;
    private Boolean canImport;
    private Boolean canExport;
}
