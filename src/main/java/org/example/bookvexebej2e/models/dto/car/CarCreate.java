package org.example.bookvexebej2e.models.dto.car;

import java.util.UUID;

import lombok.Data;

@Data
public class CarCreate {
    private String code;
    private UUID carTypeId;
    private String licensePlate;
}
