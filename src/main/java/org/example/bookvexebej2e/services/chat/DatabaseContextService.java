package org.example.bookvexebej2e.services.chat;

import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.repositories.route.RouteRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service cung cấp context từ database cho AI Chatbot
 * Giúp chatbot có thông tin thực tế về tuyến đường, chuyến xe, giá vé
 */
@Service
public class DatabaseContextService {

    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;
    private final DateTimeFormatter vietnameseFormatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy", Locale.forLanguageTag("vi-VN"));

    public DatabaseContextService(RouteRepository routeRepository, TripRepository tripRepository) {
        this.routeRepository = routeRepository;
        this.tripRepository = tripRepository;
    }

    /**
     * Lấy thông tin tổng quan về hệ thống
     */
    public String getSystemOverview() {
        long totalRoutes = routeRepository.count();
        long totalTrips = tripRepository.count();

        return String.format("""
                📊 THÔNG TIN HỆ THỐNG ĐẶT VÉ XE:
                - Tổng số tuyến đường: %d tuyến
                - Tổng số chuyến xe: %d chuyến
                - Hệ thống hỗ trợ đặt vé trực tuyến 24/7
                - Thanh toán qua nhiều phương thức (Tiền mặt, Chuyển khoản, Ví điện tử)
                - Thời gian hiện tại ở Việt Nam: %s
                """, totalRoutes, totalTrips, LocalDateTime.now().format(vietnameseFormatter));
    }

    /**
     * Lấy thông tin về tất cả các tuyến đường
     */
    public String getRoutesContext() {
        List<RouteDbModel> routes = routeRepository.findAll(PageRequest.of(0, 20, Sort.by("startLocation"))).getContent();

        if (routes.isEmpty()) {
            return "⚠️ Hiện tại chưa có thông tin tuyến đường nào trong hệ thống.";
        }

        StringBuilder context = new StringBuilder("🚍 CÁC TUYẾN ĐƯỜNG HIỆN CÓ:\n\n");

        for (RouteDbModel route : routes) {
            context.append(String.format(
                    "• %s → %s\n" +
                    "  - Khoảng cách: %.1f km\n" +
                    "  - Thời gian ước tính: %s\n\n",
                    route.getStartLocation(),
                    route.getEndLocation(),
                    route.getDistanceKm(),
                    formatDuration(route.getEstimatedDuration())
            ));
        }

        return context.toString();
    }

    /**
     * Lấy thông tin các chuyến xe sắp khởi hành
     */
    public String getUpcomingTripsContext() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusWeeks(1);

        // Tạo specification để lọc các chuyến xe trong tương lai
        Specification<TripDbModel> spec = (root, query, cb) -> {
            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("departureTime"), now),
                    cb.lessThanOrEqualTo(root.get("departureTime"), nextWeek),
                    cb.equal(root.get("isDeleted"), false)
            );
        };

        List<TripDbModel> upcomingTrips = tripRepository.findAll(spec,
                PageRequest.of(0, 15, Sort.by("departureTime"))).getContent();

        if (upcomingTrips.isEmpty()) {
            return "⚠️ Hiện tại chưa có chuyến xe nào trong 7 ngày tới.";
        }

        StringBuilder context = new StringBuilder("🕐 CÁC CHUYẾN XE SẮP KHỞI HÀNH (7 NGÀY TỚI):\n\n");

        for (TripDbModel trip : upcomingTrips) {
            if (trip.getRoute() != null) {
                context.append(String.format(
                        "• %s → %s\n" +
                        "  - Khởi hành: %s\n" +
                        "  - Giá vé: %s VNĐ\n" +
                        "  - Ghế trống: %d ghế\n\n",
                        trip.getRoute().getStartLocation(),
                        trip.getRoute().getEndLocation(),
                        trip.getDepartureTime().format(vietnameseFormatter),
                        formatPrice(trip.getPrice()),
                        trip.getAvailableSeats()
                ));
            }
        }

        return context.toString();
    }

    /**
     * Tìm kiếm tuyến đường theo điểm đi và điểm đến
     */
    public String findRoutesByLocations(String departure, String arrival) {
        // Chuẩn hóa tên địa điểm (viết hoa chữ cái đầu)
        String departureNormalized = normalizeLocation(departure);
        String arrivalNormalized = normalizeLocation(arrival);

        // Tìm kiếm tuyến đường với LIKE để linh hoạt hơn
        Specification<RouteDbModel> spec = (root, query, cb) -> {
            return cb.and(
                    cb.like(cb.lower(root.get("startLocation")), "%" + departureNormalized.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("endLocation")), "%" + arrivalNormalized.toLowerCase() + "%"),
                    cb.equal(root.get("isDeleted"), false)
            );
        };

        List<RouteDbModel> routes = routeRepository.findAll(spec);

        if (routes.isEmpty()) {
            return String.format(
                    "⚠️ Không tìm thấy tuyến đường từ %s đến %s.\n" +
                    "Bạn có thể thử tìm kiếm với tên địa điểm khác hoặc liên hệ hotline để được hỗ trợ.",
                    departureNormalized, arrivalNormalized
            );
        }

        StringBuilder context = new StringBuilder(String.format(
                "🔍 TUYẾN ĐƯỜNG TỪ %s ĐÊN %s:\n\n",
                departureNormalized.toUpperCase(),
                arrivalNormalized.toUpperCase()
        ));

        for (RouteDbModel route : routes) {
            context.append(String.format(
                    "• %s → %s\n" +
                    "  - Khoảng cách: %.1f km\n" +
                    "  - Thời gian: %s\n",
                    route.getStartLocation(),
                    route.getEndLocation(),
                    route.getDistanceKm(),
                    formatDuration(route.getEstimatedDuration())
            ));

            // Tìm các chuyến xe cho tuyến này
            String tripsInfo = findTripsByRoute(route);
            context.append(tripsInfo).append("\n");
        }

        return context.toString();
    }

    /**
     * Tìm các chuyến xe theo tuyến đường
     */
    private String findTripsByRoute(RouteDbModel route) {
        LocalDateTime now = LocalDateTime.now();

        Specification<TripDbModel> spec = (root, query, cb) -> {
            return cb.and(
                    cb.equal(root.get("route"), route),
                    cb.greaterThanOrEqualTo(root.get("departureTime"), now),
                    cb.equal(root.get("isDeleted"), false)
            );
        };

        List<TripDbModel> trips = tripRepository.findAll(spec,
                PageRequest.of(0, 5, Sort.by("departureTime"))).getContent();

        if (trips.isEmpty()) {
            return "  ⚠️ Hiện chưa có chuyến xe nào trong thời gian tới.\n";
        }

        StringBuilder tripsInfo = new StringBuilder("  📅 Các chuyến xe sắp tới:\n");
        for (TripDbModel trip : trips) {
            tripsInfo.append(String.format(
                    "     - %s | Giá: %s VNĐ | Còn %d ghế\n",
                    trip.getDepartureTime().format(vietnameseFormatter),
                    formatPrice(trip.getPrice()),
                    trip.getAvailableSeats()
            ));
        }

        return tripsInfo.toString();
    }

    /**
     * Tìm chuyến xe theo thời gian cụ thể
     */
    public String findTripsByDateTime(LocalDateTime startDate, LocalDateTime endDate) {
        Specification<TripDbModel> spec = (root, query, cb) -> {
            return cb.and(
                    cb.between(root.get("departureTime"), startDate, endDate),
                    cb.equal(root.get("isDeleted"), false)
            );
        };

        List<TripDbModel> trips = tripRepository.findAll(spec,
                PageRequest.of(0, 20, Sort.by("departureTime"))).getContent();

        if (trips.isEmpty()) {
            return String.format(
                    "⚠️ Không có chuyến xe nào từ %s đến %s",
                    startDate.format(vietnameseFormatter),
                    endDate.format(vietnameseFormatter)
            );
        }

        StringBuilder context = new StringBuilder(String.format(
                "📅 CHUYẾN XE TỪ %s ĐẾN %s:\n\n",
                startDate.format(vietnameseFormatter),
                endDate.format(vietnameseFormatter)
        ));

        for (TripDbModel trip : trips) {
            if (trip.getRoute() != null) {
                context.append(String.format(
                        "• %s → %s | %s | %s VNĐ | Còn %d ghế\n",
                        trip.getRoute().getStartLocation(),
                        trip.getRoute().getEndLocation(),
                        trip.getDepartureTime().format(vietnameseFormatter),
                        formatPrice(trip.getPrice()),
                        trip.getAvailableSeats()
                ));
            }
        }

        return context.toString();
    }

    /**
     * Lấy thống kê về giá vé cho một tuyến cụ thể
     */
    public String getPriceStatistics(String departure, String arrival) {
        String departureNormalized = normalizeLocation(departure);
        String arrivalNormalized = normalizeLocation(arrival);

        Specification<RouteDbModel> routeSpec = (root, query, cb) -> {
            return cb.and(
                    cb.like(cb.lower(root.get("startLocation")), "%" + departureNormalized.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("endLocation")), "%" + arrivalNormalized.toLowerCase() + "%"),
                    cb.equal(root.get("isDeleted"), false)
            );
        };

        List<RouteDbModel> routes = routeRepository.findAll(routeSpec);

        if (routes.isEmpty()) {
            return String.format("⚠️ Không tìm thấy tuyến đường từ %s đến %s", departureNormalized, arrivalNormalized);
        }

        // Lấy tất cả chuyến xe của các tuyến này
        List<TripDbModel> allTrips = routes.stream()
                .flatMap(route -> {
                    Specification<TripDbModel> tripSpec = (root, query, cb) -> {
                        return cb.and(
                                cb.equal(root.get("route"), route),
                                cb.greaterThanOrEqualTo(root.get("departureTime"), LocalDateTime.now()),
                                cb.equal(root.get("isDeleted"), false)
                        );
                    };
                    return tripRepository.findAll(tripSpec).stream();
                })
                .collect(Collectors.toList());

        if (allTrips.isEmpty()) {
            return "⚠️ Hiện chưa có chuyến xe nào cho tuyến này.";
        }

        // Tính toán thống kê
        var stats = allTrips.stream()
                .map(TripDbModel::getPrice)
                .mapToDouble(price -> price.doubleValue())
                .summaryStatistics();

        return String.format("""
                💰 THỐNG KÊ GIÁ VÉ TỪ %s ĐẾN %s:
                - Giá thấp nhất: %s VNĐ
                - Giá cao nhất: %s VNĐ
                - Giá trung bình: %s VNĐ
                - Tổng số chuyến: %d chuyến
                """,
                departureNormalized.toUpperCase(),
                arrivalNormalized.toUpperCase(),
                formatPrice(stats.getMin()),
                formatPrice(stats.getMax()),
                formatPrice(stats.getAverage()),
                allTrips.size()
        );
    }

    // ==================== HELPER METHODS ====================

    /**
     * Chuẩn hóa tên địa điểm (viết hoa chữ cái đầu mỗi từ)
     */
    private String normalizeLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return "";
        }

        return java.util.Arrays.stream(location.trim().split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    /**
     * Format giá tiền theo kiểu Việt Nam (VD: 150.000)
     */
    private String formatPrice(Number price) {
        if (price == null) {
            return "0";
        }
        return String.format("%,.0f", price.doubleValue()).replace(",", ".");
    }

    /**
     * Format thời gian từ phút sang giờ:phút
     */
    private String formatDuration(Integer minutes) {
        if (minutes == null || minutes == 0) {
            return "Chưa xác định";
        }

        int hours = minutes / 60;
        int mins = minutes % 60;

        if (hours > 0 && mins > 0) {
            return String.format("%d giờ %d phút", hours, mins);
        } else if (hours > 0) {
            return String.format("%d giờ", hours);
        } else {
            return String.format("%d phút", mins);
        }
    }
}

