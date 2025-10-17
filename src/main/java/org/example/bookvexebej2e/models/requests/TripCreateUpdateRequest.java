package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripCreateUpdateRequest {
    private String routeId;
    private LocalDateTime departureTime;
    private BigDecimal price;
    private Integer availableSeats;
}
