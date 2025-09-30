package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.db.PaymentMethodDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDbModel, Integer> {

    // Booking-based queries
    Page<PaymentDbModel> findByBooking(BookingDbModel booking, Pageable pageable);

    Page<PaymentDbModel> findByBookingBookingId(Integer bookingId, Pageable pageable);

    // Status-based queries
    Page<PaymentDbModel> findByStatus(String status, Pageable pageable);

    Page<PaymentDbModel> findByStatusIn(List<String> statuses, Pageable pageable);

    // Payment method queries
    Page<PaymentDbModel> findByMethod(PaymentMethodDbModel method, Pageable pageable);

    Page<PaymentDbModel> findByMethodMethodId(Integer methodId, Pageable pageable);

    // Amount range
    Page<PaymentDbModel> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);

    // Date range queries
    Page<PaymentDbModel> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<PaymentDbModel> findByPaidAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Combined status and date
    Page<PaymentDbModel> findByStatusAndCreatedAtBetween(String status, LocalDateTime start, LocalDateTime end,
        Pageable pageable);
}
