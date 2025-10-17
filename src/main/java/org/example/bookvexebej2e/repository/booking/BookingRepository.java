package org.example.bookvexebej2e.repository.booking;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends BaseRepository<BookingDbModel> {
}
