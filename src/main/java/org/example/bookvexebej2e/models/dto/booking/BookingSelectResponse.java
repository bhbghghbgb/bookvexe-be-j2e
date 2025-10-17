package org.example.bookvexebej2e.models.dto.booking;

import lombok.Data;

import java.util.UUID;

@Data
public class BookingSelectResponse {
    private UUID id;
    private String code;
    private String type;
}
