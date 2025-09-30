package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripQueryRequest extends BasePageableQueryRequest {
    private Integer routeId;
    private Integer busId;
    private LocalDateTime departureAfter;
    private LocalDateTime departureBefore;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minSeats;
}
