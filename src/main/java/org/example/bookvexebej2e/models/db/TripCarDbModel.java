package org.example.bookvexebej2e.models.db;

import java.math.BigDecimal;

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