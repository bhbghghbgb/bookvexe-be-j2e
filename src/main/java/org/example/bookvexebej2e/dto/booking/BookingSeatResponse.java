package org.example.bookvexebej2e.dto.booking;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingSeatResponse {
    private UUID id;
    private UUID bookingId;
    private UUID seatId;
    private String status;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
