package org.example.bookvexebej2e.models.db;

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
    @Column(length = 255, name = "Mã loại khách hàng")
    private String code;

    @Column(length = 255, name = "Tên loại khách hàng")
    private String name;

    @Column(columnDefinition = "TEXT", name = "Mô tả")
    private String description;

    @OneToMany(mappedBy = "customerType")
    private List<CustomerDbModel> customers;
}