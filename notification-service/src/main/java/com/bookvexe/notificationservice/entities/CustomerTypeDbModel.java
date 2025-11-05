package com.bookvexe.notificationservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customerType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerTypeDbModel extends BaseModel {
    @Column(length = 255, unique = true, name = "code")
    private String code;

    @Column(length = 255, name = "name")
    private String name;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @OneToMany(mappedBy = "customerType")
    private List<CustomerDbModel> customers;
}