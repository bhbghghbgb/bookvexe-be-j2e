package org.example.bookvexebej2e.services.booking;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.booking.BookingQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSearchRequest;
import org.example.bookvexebej2e.models.dto.booking.BookingUserCreate;
import org.springframework.data.domain.Page;

public interface BookingUserService {
    /**
     * Tạo booking mới (tự động tạo customer nếu chưa tồn tại)
     */
    BookingResponse createBooking(BookingUserCreate createDto);

    /**
     * Lấy tất cả booking của user hiện tại
     */
    List<BookingResponse> getMyBookings();

    /**
     * Lấy booking của user hiện tại với phân trang và filter
     */
    Page<BookingResponse> getMyBookings(BookingQuery query);

    /**
     * Lấy chi tiết một booking theo ID (chỉ booking của user hiện tại)
     */
    BookingResponse getBookingById(UUID id);

    /**
     * Hủy booking (chỉ hủy được booking của mình)
     */
    BookingResponse cancelBooking(UUID id);

    /**
     * Tìm kiếm booking theo mã booking, tên và số điện thoại khách hàng
     */
    BookingResponse searchBooking(BookingSearchRequest searchRequest);
}