package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookvexebej2e.models.db.embeds.CreateAudit;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class NotificationDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDbModel user;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private BookingDbModel booking;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private TripDbModel trip;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private NotificationTypeDbModel type;

    @Column(name = "channel", nullable = false, length = 20)
    private String channel = "email";

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_sent")
    private Boolean isSent = false;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Embedded
    private CreateAudit createAudit = new CreateAudit();

    public LocalDateTime getCreatedAt() {
        return createAudit != null ? createAudit.getCreatedAt() : null;
    }
}
