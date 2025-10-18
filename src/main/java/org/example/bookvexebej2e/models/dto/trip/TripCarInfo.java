package org.example.bookvexebej2e.models.dto.trip;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class TripCarInfo {
    private UUID carId;
    private BigDecimal price; // Giá riêng cho xe này (tùy chọn)
    private Integer availableSeats; // Số ghế có sẵn cho xe này (tùy chọn)
}