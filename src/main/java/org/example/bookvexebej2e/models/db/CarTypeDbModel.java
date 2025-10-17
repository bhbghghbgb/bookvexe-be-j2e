package org.example.bookvexebej2e.models.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "carTypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarTypeDbModel extends BaseModel {
    @Column(length = 255, name = "Mã loại xe")
    private String code;

    @Column(length = 255, name = "Tên loại xe")
    private String name;

    @Column(length = 255, name = "Mô tả")
    private String description;

    @Column(name = "Số ghế")
    private Integer seatCount;

    @OneToMany(mappedBy = "carType")
    private List<CarDbModel> cars;
}
