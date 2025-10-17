package org.example.bookvexebej2e.models.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleSelectResponse {
    private UUID id;
    private String code;
    private String name;
}
