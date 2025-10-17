
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
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "carTypeId")
    private CarTypeDbModel carType;

    @Column(length = 20, unique = true, name = "Biển số xe")
    private String licensePlate;

    @OneToMany(mappedBy = "car")
    private List<CarSeatDbModel> carSeats;

    @OneToMany(mappedBy = "car")
    private List<CarEmployeeDbModel> carEmployees;

    @OneToMany(mappedBy = "car")
    private List<TripCarDbModel> tripCars;
}