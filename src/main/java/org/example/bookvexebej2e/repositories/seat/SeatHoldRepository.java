package org.example.bookvexebej2e.repositories.seat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.db.SeatHoldDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatHoldRepository extends JpaRepository<SeatHoldDbModel, UUID> {

    @Query("SELECT h FROM SeatHoldDbModel h WHERE h.trip.id = :tripId AND h.car.id = :carId AND h.status = 'ACTIVE' AND h.holdUntil > :now")
    List<SeatHoldDbModel> findActiveHoldsByTripAndCar(@Param("tripId") UUID tripId, @Param("carId") UUID carId,
            @Param("now") LocalDateTime now);

    @Query("SELECT h FROM SeatHoldDbModel h WHERE h.seat.id IN :seatIds AND h.trip.id = :tripId AND h.car.id = :carId AND h.status = 'ACTIVE' AND h.holdUntil > :now")
    List<SeatHoldDbModel> findActiveHoldsForSeats(@Param("seatIds") List<UUID> seatIds, @Param("tripId") UUID tripId,
            @Param("carId") UUID carId, @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE SeatHoldDbModel h SET h.status = 'EXPIRED' WHERE h.holdUntil <= :now AND h.status = 'ACTIVE'")
    int expireOldHolds(@Param("now") LocalDateTime now);

    @Query("SELECT h FROM SeatHoldDbModel h WHERE h.holdUntil <= :now AND h.status = 'ACTIVE'")
    List<SeatHoldDbModel> findExpiredHolds(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE SeatHoldDbModel h SET h.status = 'RELEASED' WHERE h.seat.id IN :seatIds AND h.trip.id = :tripId AND h.car.id = :carId AND h.status = 'ACTIVE'")
    int releaseHoldsForSeats(@Param("seatIds") List<UUID> seatIds, @Param("tripId") UUID tripId,
            @Param("carId") UUID carId);

    @Query("SELECT h.seat.id FROM SeatHoldDbModel h WHERE h.trip.id = :tripId AND h.car.id = :carId AND h.status = 'ACTIVE' AND h.holdUntil > :now")
    List<UUID> findHeldSeatIds(@Param("tripId") UUID tripId, @Param("carId") UUID carId,
            @Param("now") LocalDateTime now);
}