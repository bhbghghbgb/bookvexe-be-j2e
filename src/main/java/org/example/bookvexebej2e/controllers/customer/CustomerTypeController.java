package org.example.bookvexebej2e.controllers.customer;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.customer.CustomerTypeCreate;
import org.example.bookvexebej2e.models.dto.customer.CustomerTypeQuery;
import org.example.bookvexebej2e.models.dto.customer.CustomerTypeResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerTypeSelectResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerTypeUpdate;
import org.example.bookvexebej2e.services.customer.CustomerTypeService;
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

    @PostMapping("/pagination")
    public ResponseEntity<Page<CustomerTypeResponse>> findAll2(@RequestBody CustomerTypeQuery query) {
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

    @PostMapping("/activate/{id}")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        customerTypeService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        customerTypeService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CustomerTypeSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(customerTypeService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<CustomerTypeSelectResponse>> findAllForSelect(CustomerTypeQuery query) {
        return ResponseEntity.ok(customerTypeService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<CustomerTypeSelectResponse>> findAllForSelect2(@RequestBody CustomerTypeQuery query) {
        return ResponseEntity.ok(customerTypeService.findAllForSelect(query));
    }

}
