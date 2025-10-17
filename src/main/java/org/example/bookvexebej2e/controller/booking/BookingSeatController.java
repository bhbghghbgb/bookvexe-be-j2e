package org.example.bookvexebej2e.controller.booking;

import org.example.bookvexebej2e.models.dto.booking.*;
import org.example.bookvexebej2e.service.booking.BookingSeatService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/booking-seats")
public class BookingSeatController {

    private final BookingSeatService bookingSeatService;

    public BookingSeatController(BookingSeatService bookingSeatService) {
        this.bookingSeatService = bookingSeatService;
    }

    @GetMapping
    public ResponseEntity<List<BookingSeatResponse>> findAll() {
        return ResponseEntity.ok(bookingSeatService.findAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<BookingSeatResponse>> findAll(BookingSeatQuery query) {
        return ResponseEntity.ok(bookingSeatService.findAll(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingSeatResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingSeatService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookingSeatResponse> create(@RequestBody BookingSeatCreate createDto) {
        return ResponseEntity.ok(bookingSeatService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingSeatResponse> update(@PathVariable UUID id, @RequestBody BookingSeatUpdate updateDto) {
        return ResponseEntity.ok(bookingSeatService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bookingSeatService.delete(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        bookingSeatService.activate(id);
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        bookingSeatService.deactivate(id);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<BookingSeatSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(bookingSeatService.findAllForSelect());
    }
}
