package org.example.bookvexebej2e.dto.employee;

import lombok.Data;

@Data
public class EmployeeCreate {
    private String code;
    private String name;
    private String email;
    private String phone;
    private String description;
}
