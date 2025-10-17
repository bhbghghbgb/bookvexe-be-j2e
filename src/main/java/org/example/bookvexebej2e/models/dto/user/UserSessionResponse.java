package org.example.bookvexebej2e.models.dto.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserSessionResponse {
    private UUID id;
    private UserResponse user;
    private String accessToken;
    private LocalDateTime expiresAt;
    private Boolean revoked;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
