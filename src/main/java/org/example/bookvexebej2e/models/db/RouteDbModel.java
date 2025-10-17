
package org.example.bookvexebej2e.models.db;

import java.math.BigDecimal;
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
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RouteDbModel extends BaseModel {
    @Column(length = 100, name = "Điểm bắt đầu")
    private String startLocation;

    @Column(length = 100, name = "Điểm kết thúc")
    private String endLocation;

    @Column(precision = 6, scale = 2, name = "Khoảng cách (km)")
    private BigDecimal distanceKm;

    @Column(name = "Thời gian ước tính (phút)")
    private Integer estimatedDuration;

    @OneToMany(mappedBy = "route")
    private List<TripDbModel> trips;
}