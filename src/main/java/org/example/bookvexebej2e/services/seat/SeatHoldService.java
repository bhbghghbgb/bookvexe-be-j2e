package org.example.bookvexebej2e.services.seat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.bookvexebej2e.models.constant.BookingStatus;
import org.example.bookvexebej2e.models.constant.SeatStatus;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.db.SeatHoldDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.seat.SeatHoldRequest;
import org.example.bookvexebej2e.models.dto.seat.SeatUpdatePayload;
import org.example.bookvexebej2e.repositories.booking.BookingRepository;
import org.example.bookvexebej2e.repositories.booking.BookingSeatRepository;
import org.example.bookvexebej2e.repositories.car.CarRepository;
import org.example.bookvexebej2e.repositories.car.CarSeatRepository;
import org.example.bookvexebej2e.repositories.seat.SeatHoldRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatHoldService {

    private final SeatHoldRepository seatHoldRepository;
    private final TripRepository tripRepository;
    private final CarRepository carRepository;
    private final CarSeatRepository carSeatRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public boolean holdSeats(SeatHoldRequest request, String sessionId, UUID userId) {
        try {
            UUID tripId = UUID.fromString(request.getTripId());
            UUID carId = UUID.fromString(request.getCarId());
            List<UUID> seatIds = request.getSeatIds().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());

            // Check if any seats are already held
            LocalDateTime now = LocalDateTime.now();
            List<SeatHoldDbModel> existingHolds = seatHoldRepository.findActiveHoldsForSeats(seatIds, tripId, carId,
                    now);

            if (!existingHolds.isEmpty()) {
                log.warn("Some seats are already held: {}", existingHolds.stream()
                        .map(h -> h.getSeat().getId().toString())
                        .collect(Collectors.toList()));
                return false;
            }

            // Get required entities
            TripDbModel trip = tripRepository.findById(tripId).orElse(null);
            CarDbModel car = carRepository.findById(carId).orElse(null);
            UserDbModel user = userId != null ? userRepository.findById(userId).orElse(null) : null;

            if (trip == null || car == null) {
                log.warn("Trip or Car not found: tripId={}, carId={}", tripId, carId);
                return false;
            }

            // Create holds for each seat
            LocalDateTime holdUntil = LocalDateTime.now().plusMinutes(5);
            for (UUID seatId : seatIds) {
                CarSeatDbModel seat = carSeatRepository.findById(seatId).orElse(null);
                if (seat != null) {
                    SeatHoldDbModel hold = new SeatHoldDbModel();
                    hold.setTrip(trip);
                    hold.setCar(car);
                    hold.setSeat(seat);
                    hold.setUser(user);
                    hold.setSessionId(sessionId);
                    hold.setHoldUntil(holdUntil);
                    hold.setStatus("ACTIVE");
                    seatHoldRepository.save(hold);
                }
            }

            // Broadcast to all clients
            broadcastSeatUpdate(tripId.toString(), carId.toString(), request.getSeatIds(), "hold", holdUntil.toString(),
                    userId != null ? userId.toString() : sessionId);

            log.info("Successfully held {} seats for trip={}, car={}", seatIds.size(), tripId, carId);
            return true;

        } catch (Exception e) {
            log.error("Error holding seats: {}", e.getMessage(), e);
            return false;
        }
    }

    @Transactional
    public boolean releaseSeats(SeatHoldRequest request, String sessionId, UUID userId) {
        try {
            UUID tripId = UUID.fromString(request.getTripId());
            UUID carId = UUID.fromString(request.getCarId());
            List<UUID> seatIds = request.getSeatIds().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());

            // Release the holds
            int released = seatHoldRepository.releaseHoldsForSeats(seatIds, tripId, carId);

            if (released > 0) {
                // Broadcast to all clients
                broadcastSeatUpdate(tripId.toString(), carId.toString(), request.getSeatIds(), "release", null,
                        userId != null ? userId.toString() : sessionId);
                log.info("Released {} seat holds for trip={}, car={}", released, tripId, carId);
                return true;
            }

            return false;

        } catch (Exception e) {
            log.error("Error releasing seats: {}", e.getMessage(), e);
            return false;
        }
    }

    public List<String> getHeldSeats(String tripId, String carId) {
        try {
            UUID tripUuid = UUID.fromString(tripId);
            UUID carUuid = UUID.fromString(carId);
            LocalDateTime now = LocalDateTime.now();

            List<UUID> heldSeatIds = seatHoldRepository.findHeldSeatIds(tripUuid, carUuid, now);
            return heldSeatIds.stream()
                    .map(UUID::toString)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error getting held seats: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public List<String> getBookedSeats(String tripId, String carId) {
        try {
            UUID tripUuid = UUID.fromString(tripId);
            UUID carUuid = UUID.fromString(carId);

            // Find all booking seats for this specific trip and car that are in BOOKED
            // status
            List<BookingSeatDbModel> bookedSeats = bookingSeatRepository.findAll((root, criteriaQuery, cb) -> {
                return cb.and(
                        cb.equal(root.get("booking").get("trip").get("id"), tripUuid),
                        cb.equal(root.get("seat").get("car").get("id"), carUuid),
                        cb.equal(root.get("status"), SeatStatus.BOOKED),
                        cb.equal(root.get("isDeleted"), false),
                        cb.equal(root.get("booking").get("isDeleted"), false));
            });

            return bookedSeats.stream()
                    .map(bs -> bs.getSeat().getId().toString())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error getting booked seats: {}", e.getMessage(), e);
            return List.of();
        }
    }

    private void broadcastSeatUpdate(String tripId, String carId, List<String> seatIds, String action, String holdUntil,
            String by) {
        try {
            SeatUpdatePayload payload = new SeatUpdatePayload();
            payload.setTripId(tripId);
            payload.setCarId(carId);
            payload.setSeatIds(seatIds);
            payload.setAction(action);
            payload.setHoldUntil(holdUntil);
            payload.setBy(by);

            String topic = "/topic/seats/" + tripId + "/" + carId;
            messagingTemplate.convertAndSend(topic, payload);

            log.info("Broadcasted seat update to topic: {}, action: {}, seats: {}", topic, action, seatIds);

        } catch (Exception e) {
            log.error("Error broadcasting seat update: {}", e.getMessage(), e);
        }
    }

    // Scheduled task to clean up expired holds every minute
    @Scheduled(fixedRate = 60000) // Run every 60 seconds
    @Transactional
    public void cleanupExpiredHolds() {
        try {
            LocalDateTime now = LocalDateTime.now();

            // Find expired holds before marking them as expired
            List<SeatHoldDbModel> expiredHolds = seatHoldRepository.findExpiredHolds(now);

            if (!expiredHolds.isEmpty()) {
                // Group by trip/car for efficient broadcasting
                var groupedHolds = expiredHolds.stream()
                        .collect(Collectors.groupingBy(
                                hold -> hold.getTrip().getId().toString() + "/" + hold.getCar().getId().toString()));

                // Mark as expired
                int expired = seatHoldRepository.expireOldHolds(now);

                // Broadcast releases
                for (var entry : groupedHolds.entrySet()) {
                    String[] parts = entry.getKey().split("/");
                    String tripId = parts[0];
                    String carId = parts[1];

                    List<String> seatIds = entry.getValue().stream()
                            .map(hold -> hold.getSeat().getId().toString())
                            .collect(Collectors.toList());

                    broadcastSeatUpdate(tripId, carId, seatIds, "release", null, "system");
                }

                log.info("Cleaned up {} expired seat holds", expired);
            }

            // Also cleanup expired bookings that are still in AWAIT_PAYMENT status
            cleanupExpiredBookings(now);

        } catch (Exception e) {
            log.error("Error cleaning up expired holds: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void cleanupExpiredBookings(LocalDateTime now) {
        try {
            // Find bookings that are still in AWAIT_PAYMENT status and created more than 5
            // minutes ago
            LocalDateTime expiryTime = now.minusMinutes(5);

            List<BookingDbModel> expiredBookings = bookingRepository.findAll((root, criteriaQuery, cb) -> {
                return cb.and(
                        cb.equal(root.get("bookingStatus"), BookingStatus.AWAIT_PAYMENT),
                        cb.equal(root.get("isDeleted"), false),
                        cb.lessThan(root.get("createdDate"), expiryTime));
            });

            if (!expiredBookings.isEmpty()) {
                log.info("Found {} expired bookings to clean up", expiredBookings.size());

                for (BookingDbModel booking : expiredBookings) {
                    // Update booking status to CANCELLED
                    booking.setBookingStatus(BookingStatus.CANCELLED);

                    // Update booking seat status from RESERVED to AVAILABLE
                    if (booking.getBookingSeats() != null && !booking.getBookingSeats().isEmpty()) {
                        for (BookingSeatDbModel bookingSeat : booking.getBookingSeats()) {
                            if (SeatStatus.RESERVED.equals(bookingSeat.getStatus())) {
                                bookingSeat.setStatus(SeatStatus.AVAILABLE);
                            }
                        }
                        bookingSeatRepository.saveAll(booking.getBookingSeats());

                        // Broadcast seat release for real-time updates
                        var tripId = booking.getTrip().getId().toString();
                        var carId = booking.getTrip().getTripCars().get(0).getCar().getId().toString(); // Assuming
                                                                                                        // first car
                        var seatIds = booking.getBookingSeats().stream()
                                .map(bs -> bs.getSeat().getId().toString())
                                .collect(Collectors.toList());

                        broadcastSeatUpdate(tripId, carId, seatIds, "release", null, "system");
                    }
                }

                bookingRepository.saveAll(expiredBookings);
                log.info("Cleaned up {} expired bookings", expiredBookings.size());
            }

        } catch (Exception e) {
            log.error("Error during expired booking cleanup", e);
        }
    }
}