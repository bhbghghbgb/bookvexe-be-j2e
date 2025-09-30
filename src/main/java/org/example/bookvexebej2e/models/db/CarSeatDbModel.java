package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_seats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarSeatDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private CarDbModel car;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "seat_position", length = 50)
    private String seatPosition;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
