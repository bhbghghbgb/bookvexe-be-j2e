package org.example.bookvexebej2e.services.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ReportServiceImpl implements ReportService {

    @PersistenceContext
    private EntityManager em;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private LocalDateTime startOfDay(String yyyyMMdd) {
        LocalDate d = LocalDate.parse(yyyyMMdd, DATE_FMT);
        return d.atStartOfDay();
    }

    private LocalDateTime endOfDay(String yyyyMMdd) {
        LocalDate d = LocalDate.parse(yyyyMMdd, DATE_FMT);
        return d.atTime(23, 59, 59);
    }

    @Override
    public List<RevenuePointResponse> revenueTime(RevenueFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<org.example.bookvexebej2e.models.db.PaymentDbModel> p = cq.from(org.example.bookvexebej2e.models.db.PaymentDbModel.class);

        // DATE(p.paidAt)
        Expression<String> dateExpr = cb.function("TO_CHAR", String.class, p.get("paidAt"), cb.literal("YYYY-MM-DD"));

        List<Predicate> predicates = new ArrayList<>();
        // not deleted
        predicates.add(cb.isFalse(p.get("isDeleted")));

        // status default SUCCESS if not provided
        if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
            predicates.add(cb.equal(p.get("status"), filter.getStatus()));
        } else {
            predicates.add(cb.equal(p.get("status"), "SUCCESS"));
        }

        // date range on paidAt
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            predicates.add(cb.between(p.get("paidAt"), startOfDay(filter.getStartDate()), endOfDay(filter.getEndDate())));
        }

        // joins optional
        if (filter.getRouteId() != null) {
            Join<?, ?> b = p.join("booking");
            Join<?, ?> t = b.join("trip");
            Join<?, ?> r = t.join("route");
            predicates.add(cb.equal(r.get("id"), filter.getRouteId()));
        }
        if (filter.getMethodId() != null) {
            Join<?, ?> m = p.join("method");
            predicates.add(cb.equal(m.get("id"), filter.getMethodId()));
        }

        cq.multiselect(dateExpr, cb.sum(p.get("amount")), cb.count(p.get("id")))
          .where(predicates.toArray(new Predicate[0]))
          .groupBy(dateExpr)
          .orderBy(cb.asc(dateExpr));

        TypedQuery<Object[]> query = em.createQuery(cq);
        List<Object[]> rows = query.getResultList();
        List<RevenuePointResponse> result = new ArrayList<>();
        for (Object[] r : rows) {
            String date = (String) r[0];
            BigDecimal total = (BigDecimal) r[1];
            Long cnt = (Long) r[2];
            result.add(new RevenuePointResponse(date, total == null ? BigDecimal.ZERO : total, cnt == null ? 0L : cnt));
        }
        return result;
    }

    @Override
    public List<RevenueByMethodResponse> revenueByMethod(RevenueFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<org.example.bookvexebej2e.models.db.PaymentDbModel> p = cq.from(org.example.bookvexebej2e.models.db.PaymentDbModel.class);
        Join<?, ?> m = p.join("method");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isFalse(p.get("isDeleted")));
        if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
            predicates.add(cb.equal(p.get("status"), filter.getStatus()));
        } else {
            predicates.add(cb.equal(p.get("status"), "SUCCESS"));
        }
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            predicates.add(cb.between(p.get("paidAt"), startOfDay(filter.getStartDate()), endOfDay(filter.getEndDate())));
        }
        if (filter.getRouteId() != null) {
            Join<?, ?> b = p.join("booking");
            Join<?, ?> t = b.join("trip");
            Join<?, ?> r = t.join("route");
            predicates.add(cb.equal(r.get("id"), filter.getRouteId()));
        }

        cq.multiselect(m.get("name"), cb.sum(p.get("amount")))
          .where(predicates.toArray(new Predicate[0]))
          .groupBy(m.get("name"))
          .orderBy(cb.desc(cb.sum(p.get("amount"))));

        List<Object[]> rows = em.createQuery(cq).getResultList();
        List<RevenueByMethodResponse> result = new ArrayList<>();
        for (Object[] r : rows) {
            String method = (String) r[0];
            BigDecimal total = (BigDecimal) r[1];
            result.add(new RevenueByMethodResponse(method, total == null ? BigDecimal.ZERO : total));
        }
        return result;
    }

    @Override
    public List<BookingsByRouteResponse> bookingsByRoute(BookingsFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<org.example.bookvexebej2e.models.db.BookingDbModel> b = cq.from(org.example.bookvexebej2e.models.db.BookingDbModel.class);
        Join<?, ?> t = b.join("trip");
        Join<?, ?> r = t.join("route");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isFalse(b.get("isDeleted")));
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            predicates.add(cb.between(b.get("createdDate"), startOfDay(filter.getStartDate()), endOfDay(filter.getEndDate())));
        }
        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            predicates.add(b.get("bookingStatus").in(filter.getStatuses()));
        }
        if (filter.getCustomerTypeId() != null) {
            Join<?, ?> c = b.join("customer");
            predicates.add(cb.equal(c.get("customerType").get("id"), filter.getCustomerTypeId()));
        }

        Expression<String> routeName = cb.concat(cb.concat(r.get("startLocation"), " \u2192 "), r.get("endLocation"));

        cq.multiselect(routeName, cb.count(b.get("id")))
          .where(predicates.toArray(new Predicate[0]))
          .groupBy(r.get("startLocation"), r.get("endLocation"))
          .orderBy(cb.desc(cb.count(b.get("id"))));

        List<Object[]> rows = em.createQuery(cq).getResultList();
        List<BookingsByRouteResponse> result = new ArrayList<>();
        for (Object[] rrow : rows) {
            String rn = (String) rrow[0];
            Long total = (Long) rrow[1];
            result.add(new BookingsByRouteResponse(rn, total == null ? 0L : total));
        }
        return result;
    }

    @Override
    public List<BookingsStatusByRouteResponse> bookingsStatusByRoute(BookingsFilter filter) {
        // We'll use JPQL CASE via CriteriaBuilder
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<org.example.bookvexebej2e.models.db.BookingDbModel> b = cq.from(org.example.bookvexebej2e.models.db.BookingDbModel.class);
        Join<?, ?> t = b.join("trip");
        Join<?, ?> r = t.join("route");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isFalse(b.get("isDeleted")));
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            predicates.add(cb.between(b.get("createdDate"), startOfDay(filter.getStartDate()), endOfDay(filter.getEndDate())));
        }
        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            predicates.add(b.get("bookingStatus").in(filter.getStatuses()));
        }
        if (filter.getCustomerTypeId() != null) {
            Join<?, ?> c = b.join("customer");
            predicates.add(cb.equal(c.get("customerType").get("id"), filter.getCustomerTypeId()));
        }

        Expression<Long> cancelled = cb.sum(cb.<Long>selectCase().when(cb.equal(b.get("bookingStatus"), "CANCELLED"), 1L).otherwise(0L));
        Expression<Long> success = cb.sum(cb.<Long>selectCase().when(b.get("bookingStatus").in("CONFIRMED", "PAID", "COMPLETED"), 1L).otherwise(0L));
        Expression<String> routeName = cb.concat(cb.concat(r.get("startLocation"), " \u2192 "), r.get("endLocation"));

        cq.multiselect(routeName, cancelled, success)
          .where(predicates.toArray(new Predicate[0]))
          .groupBy(r.get("startLocation"), r.get("endLocation"))
          .orderBy(cb.desc(success));

        List<Object[]> rows = em.createQuery(cq).getResultList();
        List<BookingsStatusByRouteResponse> result = new ArrayList<>();
        for (Object[] rr : rows) {
            String rn = (String) rr[0];
            Long cval = (Long) rr[1];
            Long sval = (Long) rr[2];
            result.add(new BookingsStatusByRouteResponse(rn, cval == null ? 0L : cval, sval == null ? 0L : sval));
        }
        return result;
    }

    @Override
    public List<TripsStatusResponse> tripsStatus(TripsFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<org.example.bookvexebej2e.models.db.BookingDbModel> b = cq.from(org.example.bookvexebej2e.models.db.BookingDbModel.class);
        Join<?, ?> t = b.join("trip");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isFalse(b.get("isDeleted")));
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            predicates.add(cb.between(t.get("departureTime"), startOfDay(filter.getStartDate()), endOfDay(filter.getEndDate())));
        }
        if (filter.getRouteId() != null) {
            predicates.add(cb.equal(t.get("route").get("id"), filter.getRouteId()));
        }
        if (filter.getCarTypeId() != null) {
            // exists subquery for tripCars with carTypeId
            jakarta.persistence.criteria.Subquery<Long> sub = cq.subquery(Long.class);
            Root<org.example.bookvexebej2e.models.db.TripCarDbModel> tc = sub.from(org.example.bookvexebej2e.models.db.TripCarDbModel.class);
            sub.select(cb.literal(1L)).where(cb.equal(tc.get("trip").get("id"), t.get("id")), cb.equal(tc.get("car").get("carType").get("id"), filter.getCarTypeId()));
            predicates.add(cb.exists(sub));
        }

        Expression<String> statusLabel = cb.selectCase().when(cb.equal(b.get("bookingStatus"), "CANCELLED"), "Bị hủy").otherwise("Đã chạy");
        cq.multiselect(statusLabel, cb.count(b.get("id")))
          .where(predicates.toArray(new Predicate[0]))
          .groupBy(statusLabel);

        List<Object[]> rows = em.createQuery(cq).getResultList();
        Map<String, Long> map = new HashMap<>();
        for (Object[] rr : rows) {
            map.put((String) rr[0], (Long) rr[1]);
        }
        List<TripsStatusResponse> result = new ArrayList<>();
        result.add(new TripsStatusResponse("Đã chạy", map.getOrDefault("Đã chạy", 0L)));
        result.add(new TripsStatusResponse("Bị hủy", map.getOrDefault("Bị hủy", 0L)));
        return result;
    }

    @Override
    public List<TripsTrendPointResponse> tripsStatusTrend(TripsFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<org.example.bookvexebej2e.models.db.BookingDbModel> b = cq.from(org.example.bookvexebej2e.models.db.BookingDbModel.class);
        Join<?, ?> t = b.join("trip");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isFalse(b.get("isDeleted")));
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            predicates.add(cb.between(t.get("departureTime"), startOfDay(filter.getStartDate()), endOfDay(filter.getEndDate())));
        }
        if (filter.getRouteId() != null) {
            predicates.add(cb.equal(t.get("route").get("id"), filter.getRouteId()));
        }
        if (filter.getCarTypeId() != null) {
            jakarta.persistence.criteria.Subquery<Long> sub = cq.subquery(Long.class);
            Root<org.example.bookvexebej2e.models.db.TripCarDbModel> tc = sub.from(org.example.bookvexebej2e.models.db.TripCarDbModel.class);
            sub.select(cb.literal(1L)).where(cb.equal(tc.get("trip").get("id"), t.get("id")), cb.equal(tc.get("car").get("carType").get("id"), filter.getCarTypeId()));
            predicates.add(cb.exists(sub));
        }

        Expression<String> dateExpr = cb.function("TO_CHAR", String.class, t.get("departureTime"), cb.literal("YYYY-MM-DD"));
        Expression<Long> cancelled = cb.sum(cb.<Long>selectCase().when(cb.equal(b.get("bookingStatus"), "CANCELLED"), 1L).otherwise(0L));
        Expression<Long> completed = cb.sum(cb.<Long>selectCase().when(cb.notEqual(b.get("bookingStatus"), "CANCELLED"), 1L).otherwise(0L));

        cq.multiselect(dateExpr, cancelled, completed)
          .where(predicates.toArray(new Predicate[0]))
          .groupBy(dateExpr)
          .orderBy(cb.asc(dateExpr));

        List<Object[]> rows = em.createQuery(cq).getResultList();
        List<TripsTrendPointResponse> result = new ArrayList<>();
        for (Object[] rr : rows) {
            String date = (String) rr[0];
            Long c = (Long) rr[1];
            Long d = (Long) rr[2];
            result.add(new TripsTrendPointResponse(date, c == null ? 0L : c, d == null ? 0L : d));
        }
        return result;
    }
}
