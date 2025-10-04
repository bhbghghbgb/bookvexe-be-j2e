package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.requests.BookingQueryRequest;
import org.example.bookvexebej2e.services.admin.BookingAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<BookingSeatDbModel>> getBookingSeats(@PathVariable Integer bookingId) {
        List<BookingSeatDbModel> seats = bookingService.getBookingSeats(bookingId);
        return ResponseEntity.ok(seats);
    }
}
