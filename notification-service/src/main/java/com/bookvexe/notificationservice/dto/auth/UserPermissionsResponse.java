package com.bookvexe.notificationservice.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * Response DTO chứa thông tin user và quyền hạn
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionsResponse {

    private UUID userId;
    private String username;
    private String email;
    private String name;
    private Boolean isAdmin;
    private Boolean isGoogle;

    /**
     * Map các module và quyền tương ứng
     * Key: module code (ví dụ: "CUSTOMER", "USER", "VEHICLE")
     * Value: ModulePermission object chứa các quyền cụ thể
     */
    private Map<String, ModulePermission> permissions;
}

