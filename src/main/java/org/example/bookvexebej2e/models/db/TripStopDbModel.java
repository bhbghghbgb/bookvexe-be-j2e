package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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