package org.example.bookvexebej2e.models.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserSessionSelectResponse {
    private UUID id;
    private UUID userId;
    private String accessToken;
}
