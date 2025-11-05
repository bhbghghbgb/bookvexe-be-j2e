package com.bookvexe.notificationservice.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentResponse {
    @JsonIgnoreProperties("payment")
    private BookingResponse booking;

    @JsonIgnoreProperties("payment")
    private InvoiceResponse invoice;

    private UUID id;
    private PaymentMethodResponse method;
    private BigDecimal amount;
    private String status;
    private String transactionCode;
    private LocalDateTime paidAt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
