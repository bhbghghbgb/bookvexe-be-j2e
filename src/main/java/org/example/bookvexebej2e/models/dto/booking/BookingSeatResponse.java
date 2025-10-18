package org.example.bookvexebej2e.models.dto.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.car.CarSeatResponse;

import lombok.Data;

@Data
public class BookingSeatResponse {
    private UUID id;
    private CarSeatResponse seat;
    private String status;
    private BigDecimal price;
    private String code;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
