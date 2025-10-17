
package org.example.bookvexebej2e.models.db;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "carEmployees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarEmployeeDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "carId")
    private CarDbModel car;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private EmployeeDbModel employee;
}