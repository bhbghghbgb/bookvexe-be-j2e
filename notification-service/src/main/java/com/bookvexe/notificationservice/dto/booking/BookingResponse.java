package com.bookvexe.notificationservice.dto.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerResponse;
import org.example.bookvexebej2e.models.dto.payment.PaymentResponse;
import org.example.bookvexebej2e.models.dto.trip.TripResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingResponse extends BasePermissionResponse {
    private UUID id;
    private String code;
    private String type;
    private CustomerResponse customer;
    private TripResponse trip;
    private List<BookingSeatResponse> bookingSeats;
    @JsonIgnoreProperties("booking")
    private PaymentResponse payment;
    private TripStopResponse pickupStop;
    private TripStopResponse dropoffStop;
    private String bookingStatus;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public BookingResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
