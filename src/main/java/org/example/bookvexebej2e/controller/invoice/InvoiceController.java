package org.example.bookvexebej2e.controller.invoice;

import org.example.bookvexebej2e.dto.invoice.*;
import org.example.bookvexebej2e.service.invoice.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        invoiceService.activate(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        invoiceService.deactivate(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/select")
    public ResponseEntity<List<InvoiceSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(invoiceService.findAllForSelect());
    }
}
