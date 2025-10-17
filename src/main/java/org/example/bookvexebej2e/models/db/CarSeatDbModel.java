package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "carSeats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarSeatDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "carId")
    private CarDbModel car;

    @Column(length = 10, name = "seatNumber")
    private String seatNumber;

    @Column(length = 50, name = "seatPosition")
    private String seatPosition;

    @OneToMany(mappedBy = "seat")
    private List<BookingSeatDbModel> bookingSeats;
}
