package org.example.bookvexebej2e.services.report;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.bookvexebej2e.models.dto.report.BookingsByRouteResponse;
import org.example.bookvexebej2e.models.dto.report.BookingsFilter;
import org.example.bookvexebej2e.models.dto.report.BookingsStatusByRouteResponse;
import org.example.bookvexebej2e.models.dto.report.RevenueByMethodResponse;
import org.example.bookvexebej2e.models.dto.report.RevenueFilter;
import org.example.bookvexebej2e.models.dto.report.RevenuePointResponse;
import org.example.bookvexebej2e.models.dto.report.TripsFilter;
import org.example.bookvexebej2e.models.dto.report.TripsStatusResponse;
import org.example.bookvexebej2e.models.dto.report.TripsTrendPointResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ReportPrintServiceImpl implements ReportPrintService {

    private final ReportService reportService;

    private byte[] exportPdf(String template, Map<String, Object> params, List<?> data) {
        try (InputStream is = getClass().getResourceAsStream(template)) {
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, params == null ? new HashMap<>() : params,
                    new JRBeanCollectionDataSource(data));
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException | java.io.IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] printRevenueTime(RevenueFilter filter) {
        List<RevenuePointResponse> rows = reportService.revenueTime(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Báo cáo Doanh thu theo thời gian");
        return exportPdf("/reports/revenue_time.jrxml", params, rows);
    }

    @Override
    public byte[] printRevenueByMethod(RevenueFilter filter) {
        List<RevenueByMethodResponse> rows = reportService.revenueByMethod(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Phân bổ doanh thu theo phương thức thanh toán");
        return exportPdf("/reports/revenue_by_method.jrxml", params, rows);
    }

    @Override
    public byte[] printBookingsByRoute(BookingsFilter filter) {
        List<BookingsByRouteResponse> rows = reportService.bookingsByRoute(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Lượt đặt vé theo tuyến");
        return exportPdf("/reports/bookings_by_route.jrxml", params, rows);
    }

    @Override
    public byte[] printBookingsStatusByRoute(BookingsFilter filter) {
        List<BookingsStatusByRouteResponse> rows = reportService.bookingsStatusByRoute(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Tỷ lệ vé hủy theo tuyến");
        return exportPdf("/reports/bookings_status_by_route.jrxml", params, rows);
    }

    @Override
    public byte[] printTripsStatus(TripsFilter filter) {
        List<TripsStatusResponse> rows = reportService.tripsStatus(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Tình trạng chuyến chạy / hủy");
        return exportPdf("/reports/trips_status.jrxml", params, rows);
    }

    @Override
    public byte[] printTripsTrend(TripsFilter filter) {
        List<TripsTrendPointResponse> rows = reportService.tripsStatusTrend(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Xu hướng hủy chuyến theo thời gian");
        return exportPdf("/reports/trips_trend.jrxml", params, rows);
    }
}
