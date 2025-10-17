package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bookingSeats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingSeatDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "bookingId")
    private BookingDbModel booking;

    @ManyToOne
    @JoinColumn(name = "seatId")
    private CarSeatDbModel seat;

    @Column(length = 20, name = "Trạng thái")
    private String status;

    @Column(precision = 10, scale = 2, name = "Giá")
    private BigDecimal price;
}