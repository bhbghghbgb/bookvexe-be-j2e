package org.example.bookvexebej2e.models.dto.employee;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EmployeeCreate {
    private String code;
    private String name;
    private String email;
    private String phone;
    private String description;
    private List<UUID> roleIds;
}
