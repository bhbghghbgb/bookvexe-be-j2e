package com.bookvexe.notificationservice.dto.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;
import org.example.bookvexebej2e.models.dto.user.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleUserResponse extends BasePermissionResponse {
    private UUID id;
    private RoleResponse role;
    private UserResponse user;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public RoleUserResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
