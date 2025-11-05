package com.bookvexe.notificationservice.dto.car;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CarSelectResponse {
    private UUID id;
    private String code;
    private String licensePlate;
    private List<CarSeatSelectResponse> carSeats;
    private CarTypeSelectResponse carType;
}
