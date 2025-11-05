package com.bookvexe.notificationservice.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserSessionSelectResponse {
    private UUID id;
    private UserResponse user;
    private String accessToken;
}
