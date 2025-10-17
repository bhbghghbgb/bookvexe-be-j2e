
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
@Table(name = "carTypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarTypeDbModel extends BaseModel {
    @Column(length = 255)
    private String code;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String description;

    private Integer seatCount;

    @OneToMany(mappedBy = "carType")
    private List<CarDbModel> cars;
}
