package org.example.bookvexebej2e.services.report;

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

public interface ReportService {
    List<RevenuePointResponse> revenueTime(RevenueFilter filter);

    List<RevenueByMethodResponse> revenueByMethod(RevenueFilter filter);

    List<BookingsByRouteResponse> bookingsByRoute(BookingsFilter filter);

    List<BookingsStatusByRouteResponse> bookingsStatusByRoute(BookingsFilter filter);

    List<TripsStatusResponse> tripsStatus(TripsFilter filter);

    List<TripsTrendPointResponse> tripsStatusTrend(TripsFilter filter);
}
