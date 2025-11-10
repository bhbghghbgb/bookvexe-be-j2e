package org.example.bookvexebej2e.services.trip;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.trip.TripCreate;
import org.example.bookvexebej2e.models.dto.trip.TripQuery;
import org.example.bookvexebej2e.models.dto.trip.TripResponse;
import org.example.bookvexebej2e.models.dto.trip.TripSelectResponse;
import org.example.bookvexebej2e.models.dto.trip.TripUpdate;
import org.springframework.data.domain.Page;

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

    Page<TripSelectResponse> findAllForSelect(TripQuery query);

    List<TripResponse> findUpcomingTrips();
}
