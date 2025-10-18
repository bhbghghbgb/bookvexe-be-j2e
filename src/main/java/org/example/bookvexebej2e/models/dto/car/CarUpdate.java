package org.example.bookvexebej2e.models.dto.car;

import java.util.UUID;

import lombok.Data;

@Data
public class CarUpdate {
    private UUID carTypeId;
    private String code;
    private String licensePlate;
}
