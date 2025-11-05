package com.bookvexe.notificationservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "carTypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarTypeDbModel extends BaseModel {
    @Column(length = 255, unique = true, name = "code")
    private String code;

    @Column(length = 255, name = "name")
    private String name;

    @Column(length = 255, name = "description")
    private String description;

    @Column(name = "seatCount")
    private Integer seatCount;

    @OneToMany(mappedBy = "carType")
    private List<CarDbModel> cars;
}
