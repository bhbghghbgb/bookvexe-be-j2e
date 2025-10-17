package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDbModel extends BaseModel {
    @Column(length = 255, name = "Mã khách hàng")
    private String code;

    @Column(length = 255, name = "Tên khách hàng")
    private String name;

    @Column(length = 100, name = "Email")
    private String email;

    @Column(length = 15, name = "Số điện thoại")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "customerTypeId")
    private CustomerTypeDbModel customerType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(mappedBy = "customer")
    private UserDbModel user;
}