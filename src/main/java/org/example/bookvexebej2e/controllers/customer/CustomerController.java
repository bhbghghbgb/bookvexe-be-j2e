package org.example.bookvexebej2e.controllers.customer;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.customer.CustomerCreate;
import org.example.bookvexebej2e.models.dto.customer.CustomerQuery;
import org.example.bookvexebej2e.models.dto.customer.CustomerResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerSelectResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerUpdate;
import org.example.bookvexebej2e.services.customer.CustomerService;
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
@RequestMapping("/admin/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<CustomerResponse>> findAll(CustomerQuery query) {
        return ResponseEntity.ok(customerService.findAll(query));
    }

    @PostMapping("/pagination")
    public ResponseEntity<Page<CustomerResponse>> findAll2(@RequestBody CustomerQuery query) {
        return ResponseEntity.ok(customerService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerCreate createDto) {
        return ResponseEntity.ok(customerService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable UUID id, @RequestBody CustomerUpdate updateDto) {
        return ResponseEntity.ok(customerService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        customerService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        customerService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        customerService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CustomerSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(customerService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<CustomerSelectResponse>> findAllForSelect(CustomerQuery query) {
        return ResponseEntity.ok(customerService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<CustomerSelectResponse>> findAllForSelect2(@RequestBody CustomerQuery query) {
        return ResponseEntity.ok(customerService.findAllForSelect(query));
    }

}
