package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "routeId")
    private RouteDbModel route;

    @Column(name = "departureTime")
    private LocalDateTime departureTime;

    @Column(precision = 10, scale = 2, name = "price")
    private BigDecimal price;

    @Column(name = "availableSeats")
    private Integer availableSeats;

    @OneToMany(mappedBy = "trip")
    private List<TripStopDbModel> tripStops;

    @OneToMany(mappedBy = "trip")
    private List<TripCarDbModel> tripCars;

    @OneToMany(mappedBy = "trip")
    private List<BookingDbModel> bookings;

    @OneToMany(mappedBy = "trip")
    private List<NotificationDbModel> notifications;
}