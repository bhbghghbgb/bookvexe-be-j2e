package org.example.bookvexebej2e.models.dto.booking;

import lombok.Data;

import java.util.UUID;

@Data
public class BookingSeatSelectResponse {
    private UUID id;
    private String status;
}
