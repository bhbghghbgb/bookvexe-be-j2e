package org.example.bookvexebej2e.models.responses;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatResponse {

    private Integer bookingSeatId;
    private Integer bookingId;
    private Integer seatId;
    private String seatNumber;
    private String seatPosition;
    private Boolean isReserved;
    private BigDecimal price;
}