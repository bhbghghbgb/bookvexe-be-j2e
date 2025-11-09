package org.example.bookvexebej2e.controllers.seat;

import java.util.List;
import java.util.Map;

import org.example.bookvexebej2e.services.seat.SeatHoldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@Slf4j
public class SeatHoldController {

    private final SeatHoldService seatHoldService;

    @GetMapping("/held/{tripId}/{carId}")
    public ResponseEntity<Map<String, Object>> getHeldSeats(
            @PathVariable String tripId,
            @PathVariable String carId) {
        try {
            List<String> heldSeats = seatHoldService.getHeldSeats(tripId, carId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "heldSeats", heldSeats,
                    "tripId", tripId,
                    "carId", carId));
        } catch (Exception e) {
            log.error("Error getting held seats: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error retrieving held seats: " + e.getMessage()));
        }
    }

    @GetMapping("/booked/{tripId}/{carId}")
    public ResponseEntity<Map<String, Object>> getBookedSeats(
            @PathVariable String tripId,
            @PathVariable String carId) {
        try {
            List<String> bookedSeats = seatHoldService.getBookedSeats(tripId, carId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "bookedSeats", bookedSeats,
                    "tripId", tripId,
                    "carId", carId));
        } catch (Exception e) {
            log.error("Error getting booked seats: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error retrieving booked seats: " + e.getMessage()));
        }
    }
}