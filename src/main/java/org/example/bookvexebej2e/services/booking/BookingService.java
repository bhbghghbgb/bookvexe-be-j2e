package org.example.bookvexebej2e.services.booking;

import org.example.bookvexebej2e.models.dto.booking.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

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
