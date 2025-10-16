package org.example.bookvexebej2e.controllers.admin;

import java.util.List;

import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.requests.BookingCreateRequest;
import org.example.bookvexebej2e.models.requests.BookingQueryRequest;
import org.example.bookvexebej2e.models.requests.BookingSeatCreateRequest;
import org.example.bookvexebej2e.models.requests.BookingUpdateRequest;
import org.example.bookvexebej2e.models.responses.BookingResponse;
import org.example.bookvexebej2e.models.responses.BookingSeatResponse;
import org.example.bookvexebej2e.services.admin.BookingAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/bookings")
@Tag(name = "Booking Admin", description = "Booking management APIs for administrators")
public class BookingAdminController extends BaseAdminController<BookingDbModel, Integer, BookingQueryRequest> {

    private final BookingAdminService bookingService;

    public BookingAdminController(BookingAdminService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    protected BaseAdminService<BookingDbModel, Integer, BookingQueryRequest> getService() {
        return bookingService;
    }

    /**
     * Lấy danh sách ghế của booking
     */
    @GetMapping("/{bookingId}/seats")
    @Operation(summary = "Get booking seats")
    public ResponseEntity<List<BookingSeatDbModel>> getBookingSeats(@PathVariable Integer bookingId) {
        List<BookingSeatDbModel> seats = bookingService.getBookingSeats(bookingId);
        return ResponseEntity.ok(seats);
    }

    /**
     * Tạo mới booking
     */
    @PostMapping
    @Operation(summary = "Create new booking")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request) {
        BookingResponse booking = bookingService.createBooking(request);
        return ResponseEntity.ok(booking);
    }

    /**
     * Cập nhật booking
     */
    @PutMapping("/{bookingId}")
    @Operation(summary = "Update booking")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Integer bookingId,
            @Valid @RequestBody BookingUpdateRequest request) {
        BookingResponse booking = bookingService.updateBooking(bookingId, request);
        return ResponseEntity.ok(booking);
    }

    /**
     * Cập nhật trạng thái booking
     */
    @PatchMapping("/{bookingId}/status")
    @Operation(summary = "Update booking status")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Integer bookingId,
            @RequestParam String status) {
        BookingResponse booking = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(booking);
    }

    /**
     * Thêm ghế vào booking
     */
    @PostMapping("/{bookingId}/seats")
    @Operation(summary = "Add seat to booking")
    public ResponseEntity<BookingSeatResponse> addSeatToBooking(
            @PathVariable Integer bookingId,
            @Valid @RequestBody BookingSeatCreateRequest request) {
        BookingSeatResponse bookingSeat = bookingService.addSeatToBooking(bookingId, request);
        return ResponseEntity.ok(bookingSeat);
    }

    /**
     * Xóa ghế khỏi booking
     */
    @DeleteMapping("/{bookingId}/seats/{seatId}")
    @Operation(summary = "Remove seat from booking")
    public ResponseEntity<Void> removeSeatFromBooking(
            @PathVariable Integer bookingId,
            @PathVariable Integer seatId) {
        bookingService.removeSeatFromBooking(bookingId, seatId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cập nhật thông tin ghế booking
     */
    @PutMapping("/{bookingId}/seats/{bookingSeatId}")
    @Operation(summary = "Update booking seat")
    public ResponseEntity<BookingSeatResponse> updateBookingSeat(
            @PathVariable Integer bookingId,
            @PathVariable Integer bookingSeatId,
            @Valid @RequestBody BookingSeatCreateRequest request) {
        BookingSeatResponse bookingSeat = bookingService.updateBookingSeat(bookingId, bookingSeatId, request);
        return ResponseEntity.ok(bookingSeat);
    }
}
