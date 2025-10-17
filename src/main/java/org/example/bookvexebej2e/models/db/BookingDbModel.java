package org.example.bookvexebej2e.models.db;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingDbModel extends BaseModel {
    @Column(length = 255)
    private String code;

    @Column(length = 255)
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

    @Column(length = 20)
    private String bookingStatus;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "booking")
    private List<BookingSeatDbModel> bookingSeats;

    @OneToOne(mappedBy = "booking")
    private PaymentDbModel payment;

    @OneToMany(mappedBy = "booking")
    private List<NotificationDbModel> notifications;
}