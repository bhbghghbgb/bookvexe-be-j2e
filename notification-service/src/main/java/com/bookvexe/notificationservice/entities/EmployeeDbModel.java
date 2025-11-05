package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDbModel extends BaseModel {
    @Column(length = 255, unique = true, name = "code")
    private String code;

    @Column(length = 255, name = "name")
    private String name;

    @Column(length = 100, name = "email")
    private String email;

    @Column(length = 15, name = "phone")
    private String phone;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @OneToOne(mappedBy = "employee")
    private UserDbModel user;

    @OneToMany(mappedBy = "employee")
    private List<CarEmployeeDbModel> carEmployees;
}