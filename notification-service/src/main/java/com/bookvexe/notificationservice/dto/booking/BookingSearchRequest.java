package com.bookvexe.notificationservice.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchRequest {
    private String bookingCode;
    private String customerName;
    private String customerPhone;
}