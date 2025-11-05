package com.bookvexe.notificationservice.dto.auth;

import lombok.Data;

@Data
public class PasswordResetConfirmRequest {
    private String token;
    private String newPassword;
}