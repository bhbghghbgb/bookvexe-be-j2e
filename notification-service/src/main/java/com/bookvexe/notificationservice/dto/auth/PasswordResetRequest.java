package com.bookvexe.notificationservice.dto.auth;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}