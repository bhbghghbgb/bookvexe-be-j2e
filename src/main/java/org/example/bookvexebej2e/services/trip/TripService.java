package org.example.bookvexebej2e.services.trip;

import org.example.bookvexebej2e.models.dto.trip.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TripService {
    List<TripResponse> findAll();

    Page<TripResponse> findAll(TripQuery query);

    TripResponse findById(UUID id);

    TripResponse create(TripCreate createDto);

    TripResponse update(UUID id, TripUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<TripSelectResponse> findAllForSelect();
}
