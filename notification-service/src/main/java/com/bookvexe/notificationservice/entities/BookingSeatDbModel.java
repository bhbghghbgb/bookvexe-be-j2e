package com.bookvexe.notificationservice.entities;

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

    @Column(length = 255, unique = true, name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private BookingDbModel booking;

    @ManyToOne
    @JoinColumn(name = "seatId")
    private CarSeatDbModel seat;

    @Column(length = 20, name = "status")
    private String status;

    @Column(precision = 10, scale = 2, name = "price")
    private BigDecimal price;
}