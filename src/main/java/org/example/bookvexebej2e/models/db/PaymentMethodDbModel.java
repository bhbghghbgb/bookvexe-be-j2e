
package org.example.bookvexebej2e.models.db;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "paymentMethods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentMethodDbModel extends BaseModel {
    @Column(length = 255, unique = true)
    private String code;

    @Column(length = 50, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    private Boolean isActive;

    @OneToMany(mappedBy = "method")
    private List<PaymentDbModel> payments;
}
