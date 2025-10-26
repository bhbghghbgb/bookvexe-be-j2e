package org.example.bookvexebej2e.controllers.trip;

import java.util.List;

import org.example.bookvexebej2e.models.dto.trip.TripUserQuery;
import org.example.bookvexebej2e.models.dto.trip.TripUserResponse;
import org.example.bookvexebej2e.services.trip.TripUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/trips")
@RequiredArgsConstructor
@Tag(name = "Trip User", description = "API tìm kiếm chuyến xe cho người dùng")
public class TripUserController {

    private final TripUserService tripUserService;

    @PostMapping("/by-route")
    @Operation(summary = "Tìm trips theo route", description = "Tìm tất cả trips thuộc các route khớp startLocation/endLocation, departureTime và numberOfSeats")
    public ResponseEntity<List<TripUserResponse>> getTripsByRoute(
            @RequestBody TripUserQuery query) {
        List<TripUserResponse> trips = tripUserService.findTripsByRoute(query);
        return ResponseEntity.ok(trips);
    }
}