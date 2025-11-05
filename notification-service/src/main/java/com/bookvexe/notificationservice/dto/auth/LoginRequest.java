package com.bookvexe.notificationservice.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private Boolean loginAsAdmin;
}
