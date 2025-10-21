package org.example.bookvexebej2e.repositories.booking;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends BaseRepository<BookingDbModel> {
    public List<BookingDbModel> findAllNotDeletedWithCustomer();
}
