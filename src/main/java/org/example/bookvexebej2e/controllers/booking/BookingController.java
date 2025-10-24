package org.example.bookvexebej2e.controllers.booking;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.ModuleCode;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.dto.booking.BookingCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSelectResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingUpdate;
import org.example.bookvexebej2e.services.booking.BookingService;
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
@RequestMapping("/admin/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.READ)
    public ResponseEntity<List<BookingResponse>> findAll() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/pagination")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.READ)
    public ResponseEntity<Page<BookingResponse>> findAll(BookingQuery query) {
        return ResponseEntity.ok(bookingService.findAll(query));
    }

    @PostMapping("/pagination")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.READ)
    public ResponseEntity<Page<BookingResponse>> findAll2(@RequestBody BookingQuery query) {
        return ResponseEntity.ok(bookingService.findAll(query));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.READ)
    public ResponseEntity<BookingResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.CREATE)
    public ResponseEntity<BookingResponse> create(@RequestBody BookingCreate createDto) {
        return ResponseEntity.ok(bookingService.create(createDto));
    }

    @PutMapping("/{id}")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.UPDATE)
    public ResponseEntity<BookingResponse> update(@PathVariable UUID id, @RequestBody BookingUpdate updateDto) {
        return ResponseEntity.ok(bookingService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bookingService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/activate/{id}")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.ACTIVATE)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        bookingService.activate(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/deactivate/{id}")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.DEACTIVATE)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        bookingService.deactivate(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<BookingSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(bookingService.findAllForSelect());
    }

    @GetMapping("/select/pagination")
    public ResponseEntity<Page<BookingSelectResponse>> findAllForSelect(BookingQuery query) {
        return ResponseEntity.ok(bookingService.findAllForSelect(query));
    }

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<BookingSelectResponse>> findAllForSelect2(@RequestBody BookingQuery query) {
        return ResponseEntity.ok(bookingService.findAllForSelect(query));
    }

    @PostMapping("/confirm/{id}")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.UPDATE)
    public ResponseEntity<BookingResponse> confirmTrip(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.confirmTrip(id));
    }

    @PostMapping("/complete-trip/{id}")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.UPDATE)
    public ResponseEntity<BookingResponse> completeTrip(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.completeTrip(id));
    }

    @PostMapping("/update-status-by-date/{id}")
    @RequirePermission(module = ModuleCode.BOOKING, action = PermissionAction.UPDATE)
    public ResponseEntity<BookingResponse> updateStatusByDate(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.updateStatusByDate(id));
    }

}
