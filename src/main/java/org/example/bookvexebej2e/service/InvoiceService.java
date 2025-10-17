package org.example.bookvexebej2e.service;

import org.example.bookvexebej2e.dto.invoice.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    List<InvoiceResponse> findAll();
    Page<InvoiceResponse> findAll(InvoiceQuery query);
    InvoiceResponse findById(UUID id);
    InvoiceResponse create(InvoiceCreate createDto);
    InvoiceResponse update(UUID id, InvoiceUpdate updateDto);
    void delete(UUID id);
    void activate(UUID id);
    void deactivate(UUID id);
    List<InvoiceSelectResponse> findAllForSelect();
}
