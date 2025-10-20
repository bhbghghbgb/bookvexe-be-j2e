package org.example.bookvexebej2e.models.dto.payment;

import lombok.Data;

@Data
public class PaymentMethodUpdate {
    private String code;
    private String name;
    private String description;
    private Boolean isDeleted;
}
