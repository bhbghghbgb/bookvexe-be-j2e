package org.example.bookvexebej2e.services.report;

import org.example.bookvexebej2e.models.dto.report.BookingsFilter;
import org.example.bookvexebej2e.models.dto.report.RevenueFilter;
import org.example.bookvexebej2e.models.dto.report.TripsFilter;

public interface ReportPrintService {
    byte[] printRevenueTime(RevenueFilter filter);
    byte[] printRevenueByMethod(RevenueFilter filter);
    byte[] printBookingsByRoute(BookingsFilter filter);
    byte[] printBookingsStatusByRoute(BookingsFilter filter);
    byte[] printTripsStatus(TripsFilter filter);
    byte[] printTripsTrend(TripsFilter filter);
}
