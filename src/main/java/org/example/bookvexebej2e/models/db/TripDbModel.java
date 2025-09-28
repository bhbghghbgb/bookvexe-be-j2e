package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookvexebej2e.models.db.embeds.CreateAudit;
import org.example.bookvexebej2e.models.db.embeds.UpdateAudit;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TripDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Integer tripId;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private RouteDbModel route;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private CarDbModel bus;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Embedded
    private CreateAudit createAudit = new CreateAudit();

    @Embedded
    private UpdateAudit updateAudit = new UpdateAudit();

    public LocalDateTime getCreatedAt() {
        return createAudit != null ? createAudit.getCreatedAt() : null;
    }

    public LocalDateTime getUpdatedAt() {
        return updateAudit != null ? updateAudit.getUpdatedAt() : null;
    }
}
