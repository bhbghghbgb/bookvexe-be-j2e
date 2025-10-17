
package org.example.bookvexebej2e.models.db;

import java.math.BigDecimal;

import jakarta.persistence.Column;
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
@Table(name = "bookingSeats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingSeatDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "bookingId")
    private BookingDbModel booking;

    @ManyToOne
    @JoinColumn(name = "seatId")
    private CarSeatDbModel seat;

    @Column(length = 20, name = "Trạng thái")
    private String status;

    @Column(precision = 10, scale = 2, name = "Giá")
    private BigDecimal price;
}