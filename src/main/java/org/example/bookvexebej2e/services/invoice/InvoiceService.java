package org.example.bookvexebej2e.services.invoice;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.invoice.InvoiceCreate;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceQuery;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceSelectResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceUpdate;
import org.springframework.data.domain.Page;

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

    Page<InvoiceSelectResponse> findAllForSelect(InvoiceQuery query);
}
