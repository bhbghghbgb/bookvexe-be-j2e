package com.bookvexe.notificationservice.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerResponse;
import org.example.bookvexebej2e.models.dto.employee.EmployeeResponse;
import org.example.bookvexebej2e.models.dto.role.RoleResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends BasePermissionResponse {
    private UUID id;
    private String username;
    private Boolean hasPassword;
    private Boolean isGoogle;
    private Boolean isAdmin;
    private String googleAccount;
    private EmployeeResponse employee;
    private CustomerResponse customer;
    private List<RoleResponse> roles;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public UserResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
