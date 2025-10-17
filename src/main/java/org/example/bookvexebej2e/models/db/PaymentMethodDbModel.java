package org.example.bookvexebej2e.models.db;

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
    @Column(length = 255, unique = true, name = "Mã phương thức thanh toán")
    private String code;

    @Column(length = 50, unique = true, name = "Tên phương thức thanh toán")
    private String name;

    @Column(length = 255, name = "Mô tả")
    private String description;

    @Column
    private Boolean isActive;

    @OneToMany(mappedBy = "method")
    private List<PaymentDbModel> payments;
}
