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
    private Integer routeId;
    private Integer busId; // carId
    private LocalDateTime departureTime;
    private BigDecimal price;
    private Integer availableSeats;
}
