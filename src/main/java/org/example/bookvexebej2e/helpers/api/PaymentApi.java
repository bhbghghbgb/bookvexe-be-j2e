package org.example.bookvexebej2e.helpers.api;

import org.example.bookvexebej2e.helpers.dto.PaymentDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface PaymentApi {
    @GetMapping("/admin/payments/{id}")
    PaymentDto findById(@PathVariable("id") UUID id);
}
