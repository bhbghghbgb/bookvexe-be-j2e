package com.bookvexe.notificationservice.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarCreate {
    private String code;
    private UUID carTypeId;
    private String licensePlate;
}
