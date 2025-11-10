package org.example.bookvexebej2e.services.trip;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.trip.TripStopCreate;
import org.example.bookvexebej2e.models.dto.trip.TripStopQuery;
import org.example.bookvexebej2e.models.dto.trip.TripStopResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopSelectResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopUpdate;
import org.springframework.data.domain.Page;

public interface TripStopService {
    List<TripStopResponse> findAll();

    Page<TripStopResponse> findAll(TripStopQuery query);

    TripStopResponse findById(UUID id);

    TripStopResponse create(TripStopCreate createDto);

    TripStopResponse update(UUID id, TripStopUpdate updateDto);

    void activate(UUID id);

    void deactivate(UUID id);

    List<TripStopSelectResponse> findAllForSelect();

    Page<TripStopSelectResponse> findAllForSelect(TripStopQuery query);

    List<TripStopSelectResponse> findAllForSelectByTrip(UUID tripId);

    void hardDelete(UUID id);
}
