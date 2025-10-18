package org.example.bookvexebej2e.services.trip;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.trip.TripCarCreate;
import org.example.bookvexebej2e.models.dto.trip.TripCarQuery;
import org.example.bookvexebej2e.models.dto.trip.TripCarResponse;
import org.example.bookvexebej2e.models.dto.trip.TripCarSelectResponse;
import org.example.bookvexebej2e.models.dto.trip.TripCarUpdate;
import org.springframework.data.domain.Page;

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

    Page<TripCarSelectResponse> findAllForSelect(TripCarQuery query);

    List<TripCarResponse> findByTripId(UUID tripId);
}
