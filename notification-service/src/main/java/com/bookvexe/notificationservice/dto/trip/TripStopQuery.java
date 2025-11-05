package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripStopQuery extends BasePageableQuery {
    private UUID tripId;
    private String stopType;
    private String location;
}
