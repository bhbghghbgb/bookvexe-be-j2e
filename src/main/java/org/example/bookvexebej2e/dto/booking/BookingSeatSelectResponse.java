package org.example.bookvexebej2e.dto.booking;

import lombok.Data;
import java.util.UUID;

@Data
public class BookingSeatSelectResponse {
    private UUID id;
    private String status;
}
