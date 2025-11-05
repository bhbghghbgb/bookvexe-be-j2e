package com.bookvexe.notificationservice.dto.base;

import lombok.Data;

@Data
public class BasePermissionResponse {
    private Boolean isCanView = true;
    private Boolean isCanEdit = false;
    private Boolean isCanDeactivate = false;
    private Boolean isCanActivate = false;
    private Boolean isCanCreate = true;
    private Boolean isCanImport = false;
    private Boolean isCanExport = false;

    public void setPermissionsByDeletedStatus(Boolean isDeleted) {
        if (isDeleted != null && isDeleted) {
            this.isCanEdit = false;
            this.isCanDeactivate = false;
            this.isCanActivate = true;
        } else {
            this.isCanEdit = true;
            this.isCanDeactivate = true;
            this.isCanActivate = false;
        }
    }
}