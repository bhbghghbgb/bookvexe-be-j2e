package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripUserQuery extends BasePageableQuery {
    private String startLocation;
    private String endLocation;
    private Integer numberOfSeats;
    private LocalDateTime departureTime;

}