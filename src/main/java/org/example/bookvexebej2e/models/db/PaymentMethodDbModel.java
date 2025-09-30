package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "method_id")
    private Integer methodId;

    @Column(name = "method_name", unique = true, nullable = false, length = 50)
    private String methodName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;
}