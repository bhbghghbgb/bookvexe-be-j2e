package org.example.bookvexebej2e.models.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Integer bookingId;
    private Integer userId;
    private String userFullName;
    private String userEmail;
    private Integer tripId;
    private String routeStartLocation;
    private String routeEndLocation;
    private LocalDateTime departureTime;
    private String bookingStatus;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BookingSeatResponse> bookingSeats;
}