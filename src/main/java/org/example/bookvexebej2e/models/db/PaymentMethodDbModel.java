package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookvexebej2e.models.db.embeds.SoftDeleteField;

@Entity
@Table(name = "payment_methods")
@Data
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

    @Embedded
    private SoftDeleteField softDelete = new SoftDeleteField();

    public Boolean getIsActive() {
        return softDelete != null ? softDelete.getIsActive() : null;
    }

    public void setIsActive(Boolean isActive) {
        if (softDelete == null)
            softDelete = new SoftDeleteField();
        softDelete.setIsActive(isActive);
    }
}