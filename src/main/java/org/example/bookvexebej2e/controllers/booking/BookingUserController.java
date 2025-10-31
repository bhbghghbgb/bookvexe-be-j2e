package org.example.bookvexebej2e.controllers.booking;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.booking.BookingQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSearchRequest;
import org.example.bookvexebej2e.models.dto.booking.BookingUserCreate;
import org.example.bookvexebej2e.services.booking.BookingUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/bookings")
@RequiredArgsConstructor
public class BookingUserController {
    private final BookingUserService bookingUserService;

    // Tìm kiếm booking theo mã, tên hoặc số điện thoại
    @PostMapping("/search")
    public ResponseEntity<BookingResponse> searchBooking(@RequestBody BookingSearchRequest searchRequest) {
        return ResponseEntity.ok(bookingUserService.searchBooking(searchRequest));
    }

    // Tạo booking mới (user tự đặt vé)
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingUserCreate createDto) {
        return ResponseEntity.ok(bookingUserService.createBooking(createDto));
    }

    // Lấy danh sách booking của user hiện tại
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        return ResponseEntity.ok(bookingUserService.getMyBookings());
    }

    // Lấy danh sách booking của user hiện tại (có phân trang)
    @GetMapping("/my-bookings/pagination")
    public ResponseEntity<Page<BookingResponse>> getMyBookings(BookingQuery query) {
        return ResponseEntity.ok(bookingUserService.getMyBookings(query));
    }

    // Lấy chi tiết 1 booking của user
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingUserService.getBookingById(id));
    }

    // Hủy booking
    @PostMapping("/cancel/{id}")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingUserService.cancelBooking(id));
    }

    // Xác nhận thanh toán booking
    @PostMapping("/confirm-payment/{id}")
    public ResponseEntity<BookingResponse> confirmPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingUserService.confirmPayment(id));
    }

    // Xác nhận thanh toán booking (khách)
    @PostMapping("/confirm-payment/guest/{id}")
    public ResponseEntity<BookingResponse> confirmPaymentGuest(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingUserService.confirmPaymentGuest(id));
    }
}