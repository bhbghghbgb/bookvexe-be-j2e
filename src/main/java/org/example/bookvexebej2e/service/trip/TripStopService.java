package org.example.bookvexebej2e.service.trip;

import org.example.bookvexebej2e.models.dto.trip.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TripStopService {
    List<TripStopResponse> findAll();

    Page<TripStopResponse> findAll(TripStopQuery query);

    TripStopResponse findById(UUID id);

    TripStopResponse create(TripStopCreate createDto);

    TripStopResponse update(UUID id, TripStopUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<TripStopSelectResponse> findAllForSelect();
}
