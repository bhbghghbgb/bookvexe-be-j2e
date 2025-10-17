package org.example.bookvexebej2e.models.dto.employee;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeSelectResponse {
    private UUID id;
    private String code;
    private String name;
    private String email;
}
