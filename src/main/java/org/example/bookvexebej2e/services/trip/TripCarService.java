package org.example.bookvexebej2e.services.trip;

import org.example.bookvexebej2e.models.dto.trip.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TripCarService {
    List<TripCarResponse> findAll();

    Page<TripCarResponse> findAll(TripCarQuery query);

    TripCarResponse findById(UUID id);

    TripCarResponse create(TripCarCreate createDto);

    TripCarResponse update(UUID id, TripCarUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<TripCarSelectResponse> findAllForSelect();
}
