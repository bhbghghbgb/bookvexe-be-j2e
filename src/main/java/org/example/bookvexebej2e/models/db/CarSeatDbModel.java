package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookvexebej2e.models.db.embeds.SoftDeleteField;

@Entity
@Table(name = "car_seats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarSeatDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private CarDbModel car;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "seat_position", length = 50)
    private String seatPosition;

    @Embedded
    private SoftDeleteField softDelete = new SoftDeleteField();

    public Boolean getIsActive() {
        return softDelete != null ? softDelete.getIsActive() : null;
    }

    public void setIsActive(Boolean isActive) {
        if (softDelete == null)
            softDelete = new SoftDeleteField();
        softDelete.setIsActive(isActive);
    }
}
