package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarCreate {
    private UUID carTypeId;
    private String licensePlate;
}
