package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carEmployees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarEmployeeDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "carId")
    private CarDbModel car;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private EmployeeDbModel employee;

    @Column(length = 20, name = "role")
    private String role;
}