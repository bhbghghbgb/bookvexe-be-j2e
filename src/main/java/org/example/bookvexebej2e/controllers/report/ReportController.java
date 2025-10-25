package org.example.bookvexebej2e.controllers.report;

import java.util.List;

import org.example.bookvexebej2e.models.dto.report.BookingsByRouteResponse;
import org.example.bookvexebej2e.models.dto.report.BookingsFilter;
import org.example.bookvexebej2e.models.dto.report.BookingsStatusByRouteResponse;
import org.example.bookvexebej2e.models.dto.report.RevenueByMethodResponse;
import org.example.bookvexebej2e.models.dto.report.RevenueFilter;
import org.example.bookvexebej2e.models.dto.report.RevenuePointResponse;
import org.example.bookvexebej2e.models.dto.report.TripsFilter;
import org.example.bookvexebej2e.models.dto.report.TripsStatusResponse;
import org.example.bookvexebej2e.models.dto.report.TripsTrendPointResponse;
import org.example.bookvexebej2e.services.report.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/revenue-time")
    public ResponseEntity<List<RevenuePointResponse>> revenueTime(@RequestBody RevenueFilter filter) {
        return ResponseEntity.ok(reportService.revenueTime(filter));
    }

    @PostMapping("/revenue-by-method")
    public ResponseEntity<List<RevenueByMethodResponse>> revenueByMethod(@RequestBody RevenueFilter filter) {
        return ResponseEntity.ok(reportService.revenueByMethod(filter));
    }

    @PostMapping("/bookings-by-route")
    public ResponseEntity<List<BookingsByRouteResponse>> bookingsByRoute(@RequestBody BookingsFilter filter) {
        return ResponseEntity.ok(reportService.bookingsByRoute(filter));
    }

    @PostMapping("/bookings-status-by-route")
    public ResponseEntity<List<BookingsStatusByRouteResponse>> bookingsStatusByRoute(@RequestBody BookingsFilter filter) {
        return ResponseEntity.ok(reportService.bookingsStatusByRoute(filter));
    }

    @PostMapping("/trips-status")
    public ResponseEntity<List<TripsStatusResponse>> tripsStatus(@RequestBody TripsFilter filter) {
        return ResponseEntity.ok(reportService.tripsStatus(filter));
    }

    @PostMapping("/trips-status-trend")
    public ResponseEntity<List<TripsTrendPointResponse>> tripsStatusTrend(@RequestBody TripsFilter filter) {
        return ResponseEntity.ok(reportService.tripsStatusTrend(filter));
    }
}
