package org.example.bookvexebej2e.models.dto.employee;

import lombok.Data;

@Data
public class EmployeeUpdate {
    private String code;
    private String name;
    private String email;
    private String phone;
    private String description;
}
