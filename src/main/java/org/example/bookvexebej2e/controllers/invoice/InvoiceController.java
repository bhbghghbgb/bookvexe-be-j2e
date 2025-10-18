package org.example.bookvexebej2e.controllers.invoice;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.invoice.InvoiceCreate;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceQuery;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceSelectResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceUpdate;
import org.example.bookvexebej2e.services.invoice.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> findAll() {
        return ResponseEntity.ok(invoiceService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<InvoiceResponse>> findAll(InvoiceQuery query) {
        return ResponseEntity.ok(invoiceService.findAll(query));
    }

    @PostMapping("/pagination")
    public ResponseEntity<Page<InvoiceResponse>> findAll2(@RequestBody InvoiceQuery query) {
        return ResponseEntity.ok(invoiceService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> create(@RequestBody InvoiceCreate createDto) {
        return ResponseEntity.ok(invoiceService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> update(@PathVariable UUID id, @RequestBody InvoiceUpdate updateDto) {
        return ResponseEntity.ok(invoiceService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        invoiceService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        invoiceService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        invoiceService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<InvoiceSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(invoiceService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<InvoiceSelectResponse>> findAllForSelect(InvoiceQuery query) {
        return ResponseEntity.ok(invoiceService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<InvoiceSelectResponse>> findAllForSelect2(@RequestBody InvoiceQuery query) {
        return ResponseEntity.ok(invoiceService.findAllForSelect(query));
    }

}
