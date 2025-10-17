package org.example.bookvexebej2e.dto.car;

import lombok.Data;
import java.util.UUID;

@Data
public class CarSelectResponse {
    private UUID id;
    private String licensePlate;
}
