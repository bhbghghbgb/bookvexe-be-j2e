package org.example.bookvexebej2e.models.dto.auth;

import lombok.Data;

@Data
public class PasswordResetConfirmRequest {
    private String token;
    private String newPassword;
}