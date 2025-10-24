package org.example.bookvexebej2e.services.trip;

import java.util.List;

import org.example.bookvexebej2e.models.dto.trip.TripUserResponse;

public interface TripUserService {
    List<TripUserResponse> findTripsByRoute(String startLocation, String endLocation);
}
