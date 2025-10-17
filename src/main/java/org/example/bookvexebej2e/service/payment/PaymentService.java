package org.example.bookvexebej2e.service.payment;

import org.example.bookvexebej2e.dto.payment.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<PaymentResponse> findAll();
    Page<PaymentResponse> findAll(PaymentQuery query);
    PaymentResponse findById(UUID id);
    PaymentResponse create(PaymentCreate createDto);
    PaymentResponse update(UUID id, PaymentUpdate updateDto);
    void delete(UUID id);
    void activate(UUID id);
    void deactivate(UUID id);
    List<PaymentSelectResponse> findAllForSelect();
}
