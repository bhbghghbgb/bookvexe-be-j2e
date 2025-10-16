package org.example.bookvexebej2e.models.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingUpdateRequest {

    @Positive(message = "User ID must be positive")
    private Integer userId;

    @Positive(message = "Trip ID must be positive")
    private Integer tripId;

    @Positive(message = "Total price must be positive")
    private BigDecimal totalPrice;

    private String bookingStatus;
}