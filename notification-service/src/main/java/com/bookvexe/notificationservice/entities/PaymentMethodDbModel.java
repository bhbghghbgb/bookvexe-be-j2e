package com.bookvexe.notificationservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "paymentMethods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentMethodDbModel extends BaseModel {
    @Column(length = 255, unique = true, name = "code")
    private String code;

    @Column(length = 50, unique = true, name = "name")
    private String name;

    @Column(length = 255, name = "description")
    private String description;

    @OneToMany(mappedBy = "method")
    private List<PaymentDbModel> payments;
}
