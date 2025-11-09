package org.example.bookvexebej2e.models.db;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "seat_holds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeatHoldDbModel extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "tripId", nullable = false)
    private TripDbModel trip;

    @ManyToOne
    @JoinColumn(name = "carId", nullable = false)
    private CarDbModel car;

    @ManyToOne
    @JoinColumn(name = "seatId", nullable = false)
    private CarSeatDbModel seat;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserDbModel user;

    @Column(name = "sessionId", length = 100)
    private String sessionId;

    @Column(name = "holdUntil", nullable = false)
    private LocalDateTime holdUntil;

    @Column(name = "status", length = 20, nullable = false)
    private String status; // "ACTIVE", "EXPIRED", "RELEASED"

    // Index on trip + car for fast lookups
    @PrePersist
    @PreUpdate
    private void validateHold() {
        if (holdUntil == null) {
            holdUntil = LocalDateTime.now().plusMinutes(5);
        }
        if (status == null) {
            status = "ACTIVE";
        }
    }
}