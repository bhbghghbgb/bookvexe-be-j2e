package com.bookvexe.notificationservice.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarUpdate {
    private UUID carTypeId;
    private String code;
    private String licensePlate;
}
