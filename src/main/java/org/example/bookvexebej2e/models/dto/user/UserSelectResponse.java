package org.example.bookvexebej2e.models.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserSelectResponse {
    private UUID id;
    private String username;
}
