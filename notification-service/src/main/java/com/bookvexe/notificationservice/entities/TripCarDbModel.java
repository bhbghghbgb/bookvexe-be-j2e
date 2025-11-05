package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tripCars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripCarDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "tripId")
    private TripDbModel trip;

    @ManyToOne
    @JoinColumn(name = "carId")
    private CarDbModel car;

    @Column(precision = 10, scale = 2, name = "price")
    private BigDecimal price;

    @Column(name = "availableSeats")
    private Integer availableSeats;
}