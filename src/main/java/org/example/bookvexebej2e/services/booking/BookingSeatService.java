package org.example.bookvexebej2e.services.booking;

import org.example.bookvexebej2e.models.dto.booking.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BookingSeatService {
    List<BookingSeatResponse> findAll();

    Page<BookingSeatResponse> findAll(BookingSeatQuery query);

    BookingSeatResponse findById(UUID id);

    BookingSeatResponse create(BookingSeatCreate createDto);

    BookingSeatResponse update(UUID id, BookingSeatUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<BookingSeatSelectResponse> findAllForSelect();
}
