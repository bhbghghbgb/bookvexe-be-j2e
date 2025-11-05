package com.bookvexe.notificationservice.dto.booking;

import lombok.Data;

import java.util.UUID;

@Data
public class BookingSelectResponse {
    private UUID id;
    private String code;
    private String type;
    private String customerName;
}
