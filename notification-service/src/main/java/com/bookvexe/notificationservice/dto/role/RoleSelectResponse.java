package com.bookvexe.notificationservice.dto.role;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleSelectResponse {
    private UUID id;
    private String code;
    private String name;
}
