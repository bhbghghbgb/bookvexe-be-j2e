package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;

import java.util.UUID;

@Data
public class TripStopSelectResponse {
    private UUID id;
    private String location;
    private String stopType;
}
