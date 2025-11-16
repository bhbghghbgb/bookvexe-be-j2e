package org.example.bookvexebej2e.services.report;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportPrintServiceImpl implements ReportPrintService {

    private final ReportService reportService;

    private static final DateTimeFormatter PRINTED_DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private byte[] exportPdf(String template, Map<String, Object> params, List<?> data) {
        try {
            // Ensure params and data are non-null
            Map<String, Object> safeParams = (params == null) ? new HashMap<>() : params;
            List<?> safeData = (data == null) ? java.util.Collections.emptyList() : data;
            if (log.isInfoEnabled()) {
                log.info("Report export start: template={}, paramsKeys={}, dataSize={}", template, safeParams.keySet(), safeData.size());
            }
            // Try to load resource via both class relative and context classloader
            InputStream is = getClass().getResourceAsStream(template);
            if (is == null) {
                String normalized = template.startsWith("/") ? template.substring(1) : template;
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl != null) {
                    is = cl.getResourceAsStream(normalized);
                }
                if (is == null) {
                    try {
                        ClassPathResource cpr = new ClassPathResource(normalized);
                        if (cpr.exists()) {
                            is = cpr.getInputStream();
                        }
                    } catch (java.io.IOException ioex) {
                        log.warn("ClassPathResource load failed for {}: {}", normalized, ioex.getMessage());
                    }
                }
            }
            if (is == null) {
                log.error("Report template not found: {}", template);
                throw new RuntimeException("Report template not found: " + template);
            }
            try (InputStream in = is) {
                JasperReport jasperReport = JasperCompileManager.compileReport(in);
                JasperPrint print = JasperFillManager.fillReport(jasperReport, safeParams,
                        new JRBeanCollectionDataSource(safeData));
                byte[] out = JasperExportManager.exportReportToPdf(print);
                if (log.isInfoEnabled()) {
                    log.info("Report export success: template={}, bytes={}", template, out == null ? 0 : out.length);
                }
                return out;
            }
        } catch (JRException | java.io.IOException e) {
            try (java.io.PrintWriter pw = new java.io.PrintWriter(
                    new java.io.FileWriter("report-errors.txt", true))) {
                pw.println("=== " + java.time.LocalDateTime.now() + " - template=" + template + " ===");
                e.printStackTrace(pw);
                pw.println();
            } catch (java.io.IOException ioex) {
                log.warn("Failed to write report error log: {}", ioex.getMessage());
            }
            log.error("Report export failed for template={}: {}", template, e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] printRevenueTime(RevenueFilter filter) {
        List<RevenuePointResponse> rows = reportService.revenueTime(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Hệ thống đặt vé xe — Báo cáo doanh thu theo thời gian");
        params.put("printedDate", LocalDate.now().format(PRINTED_DATE_FMT));
        params.put("printedBy", filter.getPrintedBy());
        params.put("fromDate", filter.getStartDate());
        params.put("toDate", filter.getEndDate());
        params.put("reportCode", "BC-DT-001");
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
        params.put("REPORT_TITLE", "Hệ thống đặt vé xe — Báo cáo lượt đặt vé theo tuyến");
        params.put("printedDate", LocalDate.now().format(PRINTED_DATE_FMT));
        params.put("printedBy", filter.getPrintedBy());
        params.put("fromDate", filter.getStartDate());
        params.put("toDate", filter.getEndDate());
        params.put("reportCode", "BC-LDV-003");
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
        params.put("REPORT_TITLE", "Hệ thống đặt vé xe — Báo cáo tình trạng chuyến xe — Chạy / Hủy");
        params.put("printedDate", LocalDate.now().format(PRINTED_DATE_FMT));
        params.put("printedBy", filter.getPrintedBy());
        params.put("fromDate", filter.getStartDate());
        params.put("toDate", filter.getEndDate());
        params.put("reportCode", "BC-TC-004");
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
