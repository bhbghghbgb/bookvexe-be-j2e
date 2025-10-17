package org.example.bookvexebej2e.models.dto.trip;

import lombok.Data;

import java.util.UUID;

@Data
public class TripStopUpdate {
    private UUID tripId;
    private String stopType;
    private String location;
    private Integer orderIndex;
}
