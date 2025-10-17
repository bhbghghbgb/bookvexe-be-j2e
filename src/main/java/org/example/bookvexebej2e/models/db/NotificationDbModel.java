package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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