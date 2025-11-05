package com.bookvexe.notificationservice.dto.employee;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeQuery extends BasePageableQuery {
    private String code;
    private String name;
    private String email;
    private String phone;
    private Boolean isDeleted;
    private List<UUID> roleIds;
    private String roleFilterMode = "OR"; // OR, AND, EXCLUDE
}
