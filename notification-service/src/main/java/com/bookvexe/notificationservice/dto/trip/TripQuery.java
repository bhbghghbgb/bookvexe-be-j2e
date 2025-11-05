package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripQuery extends BasePageableQuery {
    private UUID routeId;
    private LocalDateTime departureTime;
    private Double price;
    private Boolean isDeleted;
}
