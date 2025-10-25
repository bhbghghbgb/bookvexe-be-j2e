package org.example.bookvexebej2e.services.trip;

import java.util.ArrayList;
import java.util.List;

import org.example.bookvexebej2e.mappers.TripUserMapper;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripUserResponse;
import org.example.bookvexebej2e.repositories.booking.BookingRepository;
import org.example.bookvexebej2e.repositories.booking.BookingSeatRepository;
import org.example.bookvexebej2e.repositories.trip.TripCarRepository;
import org.example.bookvexebej2e.repositories.trip.TripUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripUserServiceImpl implements TripUserService {

    private final TripUserRepository tripUserRepository;
    private final TripCarRepository tripCarRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final BookingRepository bookingRepository;
    private final TripUserMapper tripUserMapper;

    @Override
    public List<TripUserResponse> findTripsByRoute(String startLocation, String endLocation) {
        List<TripDbModel> trips = tripUserRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();

            // Trip chưa bị xóa
            predicates.add(cb.or(
                    cb.isFalse(root.get("isDeleted")),
                    cb.isNull(root.get("isDeleted"))));

            // Join với route để filter startLocation/endLocation
            var routeJoin = root.join("route");

            if (startLocation != null && !startLocation.trim().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(routeJoin.get("startLocation")),
                        "%" + startLocation.trim().toLowerCase() + "%"));
            }

            if (endLocation != null && !endLocation.trim().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(routeJoin.get("endLocation")),
                        "%" + endLocation.trim().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        // TEST: Kiểm tra database có booking data không
        System.out.println("=== DATABASE ANALYSIS ===");

        // 1. Tổng số bookings trong database
        long totalBookings = bookingRepository.count();
        System.out.println("Total bookings in database: " + totalBookings);

        // 2. Tổng số booking seats trong database
        long totalBookingSeats = bookingSeatRepository.count();
        System.out.println("Total booking seats in database: " + totalBookingSeats);

        // 3. Nếu có booking seats, lấy mẫu để xem
        if (totalBookingSeats > 0) {
            List<BookingSeatDbModel> sampleBookingSeats = bookingSeatRepository.findAll().stream()
                    .limit(5)
                    .collect(java.util.stream.Collectors.toList());

            System.out.println("Sample booking seats:");
            for (BookingSeatDbModel bs : sampleBookingSeats) {
                System.out.println("  ID: " + bs.getId() +
                        ", Status: " + bs.getStatus() +
                        ", Booking Type: " + (bs.getBooking() != null ? bs.getBooking().getType() : "null") +
                        ", Trip ID: "
                        + (bs.getBooking() != null && bs.getBooking().getTrip() != null
                                ? bs.getBooking().getTrip().getId()
                                : "null")
                        +
                        ", Seat: " + (bs.getSeat() != null ? bs.getSeat().getSeatNumber() : "null"));
            }
        }

        // 4. Kiểm tra có trip nào có booking không
        for (TripDbModel trip : trips) {
            long bookingsForTrip = bookingRepository
                    .count((root, query, cb) -> cb.equal(root.get("trip").get("id"), trip.getId()));
            System.out.println("Trip " + trip.getId() + " has " + bookingsForTrip + " bookings");

            if (bookingsForTrip > 0) {
                List<BookingDbModel> tripBookings = bookingRepository
                        .findAll((root, query, cb) -> cb.equal(root.get("trip").get("id"), trip.getId()));
                for (BookingDbModel booking : tripBookings) {
                    System.out.println("  Booking ID: " + booking.getId() +
                            ", Type: " + booking.getType() +
                            ", Status: " + booking.getBookingStatus());
                }
            }
        }
        System.out.println("=== END DATABASE ANALYSIS ===");

        for (TripDbModel trip : trips) {
            System.out.println("=== Processing Trip ID: " + trip.getId() + " ===");

            // Debug: Kiểm tra có bao nhiêu booking seats trong database
            List<BookingSeatDbModel> allBookingSeats = bookingSeatRepository.findAll();
            System.out.println("Total booking seats in database: " + allBookingSeats.size());

            // Debug: Kiểm tra có bao nhiêu booking seats cho trip này không filter gì
            List<BookingSeatDbModel> tripBookingSeats = bookingSeatRepository.findAll((root, query, cb) -> {
                return cb.equal(root.get("booking").get("trip").get("id"), trip.getId());
            });
            System.out.println("Total booking seats for trip " + trip.getId() + ": " + tripBookingSeats.size());

            if (!tripBookingSeats.isEmpty()) {
                System.out.println("Sample booking seats for this trip:");
                for (int i = 0; i < Math.min(3, tripBookingSeats.size()); i++) {
                    BookingSeatDbModel bs = tripBookingSeats.get(i);
                    System.out.println("  - BookingSeat ID: " + bs.getId() +
                            ", Status: " + bs.getStatus() +
                            ", Booking Type: " + bs.getBooking().getType() +
                            ", Seat: " + bs.getSeat().getSeatNumber());
                }
            }

            List<TripStopDbModel> stops = trip.getTripStops();
            if (stops != null) {
                stops.size();
            }

            // Load tripCars với booking seats thông tin
            List<TripCarDbModel> cars = tripCarRepository.findByTripIdAndNotDeleted(trip.getId());

            // Load booking seats cho mỗi car để biết ghế nào đã được đặt
            for (TripCarDbModel car : cars) {
                System.out.println("  --> Processing Car: " + car.getCar().getCode() +
                        " (License: " + car.getCar().getLicensePlate() + ")");

                if (car.getCar() != null && car.getCar().getCarSeats() != null) {
                    for (CarSeatDbModel seat : car.getCar().getCarSeats()) {
                        // Query để tìm booking seats cho seat này trong trip này
                        // ENHANCED: Kiểm tra cả ONEWAY và ROUNDTRIP bookings
                        List<BookingSeatDbModel> bookingSeats = bookingSeatRepository.findAll((root, query, cb) -> {
                            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();

                            // BookingSeat chưa bị xóa và active
                            predicates.add(cb.or(
                                    cb.isFalse(root.get("isDeleted")),
                                    cb.isNull(root.get("isDeleted"))));

                            // Seat match
                            predicates.add(cb.equal(root.get("seat").get("id"), seat.getId()));

                            // Booking thuộc trip này (thông qua booking -> trip)
                            predicates.add(cb.equal(root.get("booking").get("trip").get("id"), trip.getId()));

                            // Booking chưa bị xóa
                            predicates.add(cb.or(
                                    cb.isFalse(root.get("booking").get("isDeleted")),
                                    cb.isNull(root.get("booking").get("isDeleted"))));

                            // Status của booking seat không phải cancelled
                            predicates.add(cb.and(
                                    cb.isNotNull(root.get("status")),
                                    cb.notEqual(root.get("status"), "cancelled")));

                            // ENHANCED: Chấp nhận cả ONEWAY và ROUNDTRIP
                            // (Không filter theo booking type - cho phép cả 2 loại)

                            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
                        });

                        // Debug: chỉ in cho ghế A1 để tránh spam
                        if (seat.getSeatNumber().equals("A1")) {
                            System.out.println("    Seat A1 in Car " + car.getCar().getCode() +
                                    " - Found " + bookingSeats.size() + " booking seats");
                            if (!bookingSeats.isEmpty()) {
                                for (BookingSeatDbModel bookingSeat : bookingSeats) {
                                    System.out.println("      - Booking ID: " + bookingSeat.getBooking().getId() +
                                            ", Type: " + bookingSeat.getBooking().getType() +
                                            ", Booking Status: " + bookingSeat.getBooking().getBookingStatus() +
                                            ", Seat Status: " + bookingSeat.getStatus());
                                }
                            } else {
                                System.out.println("      - No booking seats found for A1");
                            }
                        }

                        // Set booking seats vào seat để mapper có thể access
                        seat.setBookingSeats(bookingSeats);
                    }
                }
            }

            trip.setTripCars(cars);
        }

        return trips.stream()
                .map(tripUserMapper::toUserResponse)
                .toList();
    }

}