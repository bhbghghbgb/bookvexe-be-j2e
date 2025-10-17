package org.example.bookvexebej2e.models.db;

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

    @Column(name = "Thời gian khởi hành")
    private LocalDateTime departureTime;

    @Column(precision = 10, scale = 2, name = "Giá")
    private BigDecimal price;

    @Column(name = "Ghế còn trống")
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