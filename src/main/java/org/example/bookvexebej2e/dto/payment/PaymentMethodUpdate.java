package org.example.bookvexebej2e.dto.payment;

import lombok.Data;

@Data
public class PaymentMethodUpdate {
    private String code;
    private String name;
    private String description;
    private Boolean isActive;
}
