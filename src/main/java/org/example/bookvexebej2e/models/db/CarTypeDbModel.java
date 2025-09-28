package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_types")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarTypeDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_type_id")
    private Integer carTypeId;

    @Column(name = "type_name", unique = true, nullable = false, length = 50)
    private String typeName;

    @Column(name = "description", length = 255)
    private String description;
}