package org.example.bookvexebej2e.models.dto.auth;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}