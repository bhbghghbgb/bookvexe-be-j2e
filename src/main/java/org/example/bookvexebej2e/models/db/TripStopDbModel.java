
package org.example.bookvexebej2e.models.db;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tripStops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripStopDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "tripId")
    private TripDbModel trip;

    @Column(length = 10, name = "Loại điểm dừng")
    private String stopType;

    @Column(length = 255, name = "Địa điểm")
    private String location;

    @Column(name = "Chỉ số thứ tự")
    private Integer orderIndex;

    @OneToMany(mappedBy = "pickupStop")
    private List<BookingDbModel> pickupBookings;

    @OneToMany(mappedBy = "dropoffStop")
    private List<BookingDbModel> dropoffBookings;
}