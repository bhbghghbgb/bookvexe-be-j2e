
package org.example.bookvexebej2e.models.db;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @Column(length = 10, name = "Số ghế")
    private String seatNumber;

    @Column(length = 50, name = "Vị trí ghế")
    private String seatPosition;

    @OneToMany(mappedBy = "seat")
    private List<BookingSeatDbModel> bookingSeats;
}
