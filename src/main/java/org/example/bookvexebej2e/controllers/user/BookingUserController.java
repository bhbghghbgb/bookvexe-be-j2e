package org.example.bookvexebej2e.controllers.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.bookvexebej2e.models.requests.BookingCreateRequest;
import org.example.bookvexebej2e.models.requests.BookingQueryRequest;
import org.example.bookvexebej2e.models.requests.BookingSeatCreateRequest;
import org.example.bookvexebej2e.models.responses.BookingResponse;
import org.example.bookvexebej2e.models.responses.BookingSeatResponse;
import org.example.bookvexebej2e.services.user.BookingUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Booking User", description = "Booking APIs for customers")
@CrossOrigin(origins = "*")
public class BookingUserController {

    private final BookingUserService bookingUserService;

    public BookingUserController(BookingUserService bookingUserService) {
        this.bookingUserService = bookingUserService;
    }

    /**
     * Tạo booking mới
     */
    @PostMapping
    @Operation(summary = "Create new booking")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request,
        @AuthenticationPrincipal Jwt jwt) {
        BookingResponse booking = bookingUserService.createBooking(request, jwt);
        return ResponseEntity.ok(booking);
    }

    /**
     * Lấy danh sách booking của user hiện tại
     */
    @GetMapping("/my-bookings")
    @Operation(summary = "Get current user bookings")
    public ResponseEntity<Page<BookingResponse>> getMyBookings(@ModelAttribute BookingQueryRequest queryRequest,
        @AuthenticationPrincipal Jwt jwt) {
        Page<BookingResponse> bookings = bookingUserService.getMyBookings(queryRequest, jwt);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Lấy booking theo ID (chỉ booking của user hiện tại)
     */
    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Integer bookingId,
        @AuthenticationPrincipal Jwt jwt) {
        BookingResponse booking = bookingUserService.getBookingById(bookingId, jwt);
        return ResponseEntity.ok(booking);
    }

    /**
     * Hủy booking
     */
    @PatchMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel booking")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Integer bookingId,
        @AuthenticationPrincipal Jwt jwt) {
        BookingResponse booking = bookingUserService.cancelBooking(bookingId, jwt);
        return ResponseEntity.ok(booking);
    }

    /**
     * Lấy booking theo trip ID (public để hiển thị thông tin booking)
     */
    @GetMapping("/trip/{tripId}")
    @Operation(summary = "Get bookings by trip ID")
    public ResponseEntity<Page<BookingResponse>> getBookingsByTripId(@PathVariable Integer tripId,
        @ModelAttribute BookingQueryRequest queryRequest) {
        Page<BookingResponse> bookings = bookingUserService.getBookingsByTripId(tripId, queryRequest);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Thêm ghế vào booking của user hiện tại
     */
    @PostMapping("/{bookingId}/seats")
    @Operation(summary = "Add seat to my booking")
    public ResponseEntity<BookingSeatResponse> addSeatToMyBooking(@PathVariable Integer bookingId,
        @Valid @RequestBody BookingSeatCreateRequest request, @AuthenticationPrincipal Jwt jwt) {
        BookingSeatResponse bookingSeat = bookingUserService.addSeatToMyBooking(bookingId, request, jwt);
        return ResponseEntity.ok(bookingSeat);
    }

    /**
     * Xóa ghế khỏi booking của user hiện tại
     */
    @DeleteMapping("/{bookingId}/seats/{seatId}")
    @Operation(summary = "Remove seat from my booking")
    public ResponseEntity<Void> removeSeatFromMyBooking(@PathVariable Integer bookingId, @PathVariable Integer seatId
        , @AuthenticationPrincipal Jwt jwt) {
        bookingUserService.removeSeatFromMyBooking(bookingId, seatId, jwt);
        return ResponseEntity.noContent()
            .build();
    }
}