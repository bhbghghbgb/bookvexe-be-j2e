package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_seats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_seat_id")
    private Integer bookingSeatId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private BookingDbModel booking;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private CarSeatDbModel seat;

    @Column(name = "is_reserved")
    private Boolean isReserved = true;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
