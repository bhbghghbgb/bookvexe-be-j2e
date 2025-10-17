package org.example.bookvexebej2e.service.booking;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.booking.BookingCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSelectResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingUpdate;
import org.springframework.data.domain.Page;

public interface BookingService {
    List<BookingResponse> findAll();

    Page<BookingResponse> findAll(BookingQuery query);

    BookingResponse findById(UUID id);

    BookingResponse create(BookingCreate createDto);

    BookingResponse update(UUID id, BookingUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<BookingSelectResponse> findAllForSelect();
}
