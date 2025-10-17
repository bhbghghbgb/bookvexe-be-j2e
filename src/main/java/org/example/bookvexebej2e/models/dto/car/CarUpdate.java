package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarUpdate {
    private UUID carTypeId;
    private String licensePlate;
}
