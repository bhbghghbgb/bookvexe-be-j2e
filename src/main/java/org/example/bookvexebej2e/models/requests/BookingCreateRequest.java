package org.example.bookvexebej2e.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Integer userId;

    @NotNull(message = "Trip ID is required")
    @Positive(message = "Trip ID must be positive")
    private Integer tripId;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be positive")
    private BigDecimal totalPrice;

    private String bookingStatus = "pending";

    // List of seat IDs to book
    private List<Integer> seatIds;

    // List of booking seats with individual prices
    private List<BookingSeatCreateRequest> bookingSeats;
}