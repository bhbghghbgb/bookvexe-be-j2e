package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookvexebej2e.models.db.embeds.CreateAudit;
import org.example.bookvexebej2e.models.db.embeds.UpdateAudit;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RouteDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId;

    @Column(name = "start_location", nullable = false, length = 100)
    private String startLocation;

    @Column(name = "end_location", nullable = false, length = 100)
    private String endLocation;

    @Column(name = "distance_km", precision = 6, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

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
