package org.example.bookvexebej2e.dto.employee;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EmployeeResponse {
    private UUID id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
