package com.bookvexe.notificationservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RouteDbModel extends BaseModel {
    @Column(length = 100, name = "startLocation")
    private String startLocation;

    @Column(length = 100, name = "endLocation")
    private String endLocation;

    @Column(precision = 6, scale = 2, name = "distanceKm")
    private BigDecimal distanceKm;

    @Column(name = "estimatedDuration")
    private Integer estimatedDuration;

    @OneToMany(mappedBy = "route")
    private List<TripDbModel> trips;
}