package org.example.bookvexebej2e.controllers.customer;

import org.example.bookvexebej2e.models.dto.customer.*;
import org.example.bookvexebej2e.services.customer.CustomerTypeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/customer-types")
public class CustomerTypeController {

    private final CustomerTypeService customerTypeService;

    public CustomerTypeController(CustomerTypeService customerTypeService) {
        this.customerTypeService = customerTypeService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerTypeResponse>> findAll() {
        return ResponseEntity.ok(customerTypeService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<CustomerTypeResponse>> findAll(CustomerTypeQuery query) {
        return ResponseEntity.ok(customerTypeService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerTypeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerTypeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerTypeResponse> create(@RequestBody CustomerTypeCreate createDto) {
        return ResponseEntity.ok(customerTypeService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerTypeResponse> update(@PathVariable UUID id,
        @RequestBody CustomerTypeUpdate updateDto) {
        return ResponseEntity.ok(customerTypeService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        customerTypeService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        customerTypeService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        customerTypeService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CustomerTypeSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(customerTypeService.findAllForSelect());
    }
}
