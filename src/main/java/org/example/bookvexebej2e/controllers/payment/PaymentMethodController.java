package org.example.bookvexebej2e.controllers.payment;

import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.payment.*;
import org.example.bookvexebej2e.services.payment.PaymentMethodService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.READ)
    public ResponseEntity<List<PaymentMethodResponse>> findAll() {
        return ResponseEntity.ok(paymentMethodService.findAll());
    }

    @GetMapping("/pagination")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.READ)
    public ResponseEntity<Page<PaymentMethodResponse>> findAll(PaymentMethodQuery query) {
        return ResponseEntity.ok(paymentMethodService.findAll(query));
    }

    @PostMapping("/pagination")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.READ)
    public ResponseEntity<Page<PaymentMethodResponse>> findAll2(@RequestBody PaymentMethodQuery query) {
        return ResponseEntity.ok(paymentMethodService.findAll(query));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.READ)
    public ResponseEntity<PaymentMethodResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentMethodService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.CREATE)
    public ResponseEntity<PaymentMethodResponse> create(@RequestBody PaymentMethodCreate createDto) {
        return ResponseEntity.ok(paymentMethodService.create(createDto));
    }

    @PutMapping("/{id}")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.UPDATE)
    public ResponseEntity<PaymentMethodResponse> update(@PathVariable UUID id,
            @RequestBody PaymentMethodUpdate updateDto) {
        return ResponseEntity.ok(paymentMethodService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        paymentMethodService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.ACTIVATE)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        paymentMethodService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.DEACTIVATE)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        paymentMethodService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.READ)
    public ResponseEntity<List<PaymentMethodSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(paymentMethodService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    @RequirePermission(module = ModuleCode.PAYMENT_METHOD, action = PermissionAction.READ)
    public ResponseEntity<Page<PaymentMethodSelectResponse>> findAllForSelect(PaymentMethodQuery query) {
        return ResponseEntity.ok(paymentMethodService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<PaymentMethodSelectResponse>> findAllForSelect2(@RequestBody PaymentMethodQuery query) {
        return ResponseEntity.ok(paymentMethodService.findAllForSelect(query));
    }

}
