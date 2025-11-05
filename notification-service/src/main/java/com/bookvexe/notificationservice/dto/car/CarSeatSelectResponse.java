package com.bookvexe.notificationservice.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarSeatSelectResponse {
    private UUID id;
    private String seatNumber;
    private String seatPosition;
}
