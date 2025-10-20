package org.example.bookvexebej2e.models.dto.trip;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;

import java.util.UUID;

@Data
public class TripCarSelectResponse {
    private UUID id;
    //    private TripResponse trip;
    private CarSelectResponse car;
}
