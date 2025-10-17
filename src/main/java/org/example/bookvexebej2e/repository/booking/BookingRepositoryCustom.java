package org.example.bookvexebej2e.repository.booking;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingRepositoryCustom {
    List<BookingDbModel> findFuzzy(String searchTerm);

    Page<BookingDbModel> findFuzzy(String searchTerm, Pageable pageable);
}
