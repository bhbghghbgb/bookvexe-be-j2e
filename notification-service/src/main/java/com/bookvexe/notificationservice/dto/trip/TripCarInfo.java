package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TripCarInfo {
    private UUID carId;
    private BigDecimal price; // Giá riêng cho xe này (tùy chọn)
    private Integer availableSeats; // Số ghế có sẵn cho xe này (tùy chọn)
}