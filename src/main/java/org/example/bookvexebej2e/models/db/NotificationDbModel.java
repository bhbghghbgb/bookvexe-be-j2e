
package org.example.bookvexebej2e.models.db;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserDbModel user;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private BookingDbModel booking;

    @ManyToOne
    @JoinColumn(name = "tripId")
    private TripDbModel trip;

    @ManyToOne
    @JoinColumn(name = "typeId")
    private NotificationTypeDbModel type;

    @Column(length = 20, name = "Kênh")
    private String channel;

    @Column(length = 100, name = "Tiêu đề")
    private String title;

    @Column(columnDefinition = "TEXT", name = "Nội dung")
    private String message;

    @Column(name = "Đã gửi")
    private Boolean isSent;

    @Column(name = "Thời gian gửi")
    private LocalDateTime sentAt;
}