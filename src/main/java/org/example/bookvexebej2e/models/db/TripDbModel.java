
package org.example.bookvexebej2e.models.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private LocalDateTime departureTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

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