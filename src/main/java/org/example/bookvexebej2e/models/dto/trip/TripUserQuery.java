package org.example.bookvexebej2e.models.dto.trip;

import java.time.LocalDateTime;

import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripUserQuery extends BasePageableQuery {
    private String startLocation;
    private String endLocation;
    private Integer numberOfSeats;
    private LocalDateTime departureTime;

}