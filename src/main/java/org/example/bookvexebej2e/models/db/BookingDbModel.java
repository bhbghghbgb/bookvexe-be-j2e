package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingDbModel extends BaseModel {
    @Column(length = 255, name = "code")
    private String code;

    @Column(length = 255, name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserDbModel user;

    @ManyToOne
    @JoinColumn(name = "tripId")
    private TripDbModel trip;

    @ManyToOne
    @JoinColumn(name = "pickupStopId")
    private TripStopDbModel pickupStop;

    @ManyToOne
    @JoinColumn(name = "dropoffStopId")
    private TripStopDbModel dropoffStop;

    @Column(length = 20, name = "bookingStatus")
    private String bookingStatus = "pending";

    @Column(precision = 10, scale = 2, name = "totalPrice")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "booking")
    private List<BookingSeatDbModel> bookingSeats;

    @OneToOne(mappedBy = "booking")
    private PaymentDbModel payment;

    @OneToMany(mappedBy = "booking")
    private List<NotificationDbModel> notifications;
}