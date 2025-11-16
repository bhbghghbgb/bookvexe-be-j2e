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
import org.example.bookvexebej2e.services.report.ReportPrintService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private final ReportPrintService reportPrintService;

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

    @PostMapping(value = "/print/revenue-time", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printRevenueTime(@RequestBody RevenueFilter filter) {
        byte[] pdf = reportPrintService.printRevenueTime(filter);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=doanh_thu_theo_thoi_gian.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    @PostMapping(value = "/print/revenue-by-method", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printRevenueByMethod(@RequestBody RevenueFilter filter) {
        byte[] pdf = reportPrintService.printRevenueByMethod(filter);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=doanh_thu_theo_phuong_thuc.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    @PostMapping(value = "/print/bookings-by-route", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printBookingsByRoute(@RequestBody BookingsFilter filter) {
        byte[] pdf = reportPrintService.printBookingsByRoute(filter);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=luot_dat_ve_theo_tuyen.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    @PostMapping(value = "/print/bookings-status-by-route", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printBookingsStatusByRoute(@RequestBody BookingsFilter filter) {
        byte[] pdf = reportPrintService.printBookingsStatusByRoute(filter);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ty_le_huy_ve_theo_tuyen.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    @PostMapping(value = "/print/trips-status", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printTripsStatus(@RequestBody TripsFilter filter) {
        byte[] pdf = reportPrintService.printTripsStatus(filter);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tinh_trang_chuyen_chay_huy.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    @PostMapping(value = "/print/trips-status-trend", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printTripsTrend(@RequestBody TripsFilter filter) {
        byte[] pdf = reportPrintService.printTripsTrend(filter);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=xu_huong_huy_chuyen.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }
}
