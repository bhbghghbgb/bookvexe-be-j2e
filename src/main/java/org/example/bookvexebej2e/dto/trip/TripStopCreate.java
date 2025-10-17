package org.example.bookvexebej2e.dto.trip;

import lombok.Data;
import java.util.UUID;

@Data
public class TripStopCreate {
    private UUID tripId;
    private String stopType;
    private String location;
    private Integer orderIndex;
}
