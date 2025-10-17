package org.example.bookvexebej2e.controller.booking;

import org.example.bookvexebej2e.dto.booking.*;
import org.example.bookvexebej2e.service.booking.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    @GetMapping
    public ResponseEntity<List<BookingResponse>> findAll() {
        return ResponseEntity.ok(bookingService.findAll());
    }
    
    @GetMapping("/pagination")
    public ResponseEntity<Page<BookingResponse>> findAll(BookingQuery query) {
        return ResponseEntity.ok(bookingService.findAll(query));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody BookingCreate createDto) {
        return ResponseEntity.ok(bookingService.create(createDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable UUID id, @RequestBody BookingUpdate updateDto) {
        return ResponseEntity.ok(bookingService.update(id, updateDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bookingService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        bookingService.activate(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        bookingService.deactivate(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/select")
    public ResponseEntity<List<BookingSelectResponse>> findAllForSelect() {
        return ResponseEntity.ok(bookingService.findAllForSelect());
    }
}
