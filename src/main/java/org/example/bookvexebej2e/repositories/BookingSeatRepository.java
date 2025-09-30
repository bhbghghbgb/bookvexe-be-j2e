package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeatDbModel, Integer> {
    List<BookingSeatDbModel> findByBooking(BookingDbModel booking);

    List<BookingSeatDbModel> findBySeat(CarSeatDbModel seat);

    List<BookingSeatDbModel> findByIsReserved(Boolean isReserved);
}
