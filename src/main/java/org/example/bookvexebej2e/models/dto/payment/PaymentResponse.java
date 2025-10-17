package org.example.bookvexebej2e.models.dto.payment;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentResponse {
    private UUID id;
    private BookingResponse booking;
    private PaymentMethodResponse method;
    private BigDecimal amount;
    private String status;
    private String transactionCode;
    private LocalDateTime paidAt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
