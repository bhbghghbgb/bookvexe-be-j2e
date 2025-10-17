package org.example.bookvexebej2e.dto.user;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserSessionCreate {
    private UUID userId;
    private String accessToken;
    private LocalDateTime expiresAt;
    private Boolean revoked;
}
