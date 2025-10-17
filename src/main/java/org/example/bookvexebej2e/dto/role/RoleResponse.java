package org.example.bookvexebej2e.dto.role;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
