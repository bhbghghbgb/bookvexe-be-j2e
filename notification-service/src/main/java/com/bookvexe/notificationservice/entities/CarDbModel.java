package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(length = 255, unique = true, name = "code")
    private String code;

    @Column(length = 20, unique = true, name = "licensePlate")
    private String licensePlate;

    @OneToMany(mappedBy = "car")
    private List<CarSeatDbModel> carSeats;

    @OneToMany(mappedBy = "car")
    private List<CarEmployeeDbModel> carEmployees;

    @OneToMany(mappedBy = "car")
    private List<TripCarDbModel> tripCars;
}