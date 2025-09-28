package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookvexebej2e.models.db.embeds.CreateAudit;
import org.example.bookvexebej2e.models.db.embeds.UpdateAudit;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BookingDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDbModel user;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private TripDbModel trip;

    @Column(name = "booking_status", nullable = false, length = 20)
    private String bookingStatus = "pending";

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

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
