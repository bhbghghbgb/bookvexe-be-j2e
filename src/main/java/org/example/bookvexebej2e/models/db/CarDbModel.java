package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "cars")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CarDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer carId;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserDbModel owner;

    @ManyToOne
    @JoinColumn(name = "car_type_id")
    private CarTypeDbModel carType;

    @Column(name = "license_plate", unique = true, nullable = false, length = 20)
    private String licensePlate;

    @Column(name = "seat_count", nullable = false)
    private Integer seatCount;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
