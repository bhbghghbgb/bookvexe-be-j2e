package com.bookvexe.notificationservice.dto.payment;

import lombok.Data;

@Data
public class PaymentMethodUpdate {
    private String code;
    private String name;
    private String description;
    private Boolean isDeleted;
}
