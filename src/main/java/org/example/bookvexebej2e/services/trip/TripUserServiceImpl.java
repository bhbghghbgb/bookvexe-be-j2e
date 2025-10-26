package org.example.bookvexebej2e.services.trip;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.bookvexebej2e.mappers.TripUserMapper;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripUserQuery;
import org.example.bookvexebej2e.models.dto.trip.TripUserResponse;
import org.example.bookvexebej2e.repositories.booking.BookingSeatRepository;
import org.example.bookvexebej2e.repositories.trip.TripCarRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripUserServiceImpl implements TripUserService {

    private final TripRepository tripUserRepository;
    private final TripCarRepository tripCarRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final TripUserMapper tripUserMapper;

    @Override
    public List<TripUserResponse> findTripsByRoute(TripUserQuery query) {
        String startLocation = query.getStartLocation();
        String endLocation = query.getEndLocation();
        Integer numberOfSeats = query.getNumberOfSeats();
        var departureTime = query.getDepartureTime();

        List<TripDbModel> trips = tripUserRepository.findAll((root, criteriaQuery, cb) -> {
            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(cb.or(
                    cb.isFalse(root.get("isDeleted")),
                    cb.isNull(root.get("isDeleted"))));
            var routeJoin = root.join("route");
            if (startLocation != null && !startLocation.trim().isEmpty()) {
                String normalized = removeVietnameseAccents(startLocation.trim().toLowerCase());
                predicates.add(cb.or(
                        cb.equal(
                                cb.lower(cb.trim(routeJoin.get("startLocation"))),
                                startLocation.trim().toLowerCase()),
                        cb.like(
                                cb.lower(cb.trim(routeJoin.get("startLocation"))),
                                "%" + startLocation.trim().toLowerCase() + "%")));
            }

            if (endLocation != null && !endLocation.trim().isEmpty()) {
                String normalized = removeVietnameseAccents(endLocation.trim().toLowerCase());
                predicates.add(cb.or(
                        cb.equal(
                                cb.lower(cb.trim(routeJoin.get("endLocation"))),
                                endLocation.trim().toLowerCase()),
                        cb.like(
                                cb.lower(cb.trim(routeJoin.get("endLocation"))),
                                "%" + endLocation.trim().toLowerCase() + "%")));
            }
            if (departureTime != null) {
                var startOfDay = departureTime.toLocalDate().atStartOfDay();
                var endOfDay = departureTime.toLocalDate().atTime(23, 59, 59);
                predicates.add(cb.between(
                        root.get("departureTime"),
                        startOfDay,
                        endOfDay));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });
        for (TripDbModel trip : trips) {
            enrichTripWithBookingData(trip);
        }

        return trips.stream()
                .map(tripUserMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    private void enrichTripWithBookingData(TripDbModel trip) {
        if (trip.getTripStops() != null) {
            trip.getTripStops().size();
        }
        List<TripCarDbModel> cars = tripCarRepository.findByTripIdAndNotDeleted(trip.getId());
        for (TripCarDbModel car : cars) {
            if (car.getCar() != null && car.getCar().getCarSeats() != null) {
                for (CarSeatDbModel seat : car.getCar().getCarSeats()) {
                    List<BookingSeatDbModel> bookingSeats = findBookingSeatsForSeat(trip.getId(), seat.getId());
                    seat.setBookingSeats(bookingSeats);
                }
            }
        }

        trip.setTripCars(cars);
    }

    private List<BookingSeatDbModel> findBookingSeatsForSeat(java.util.UUID tripId, java.util.UUID seatId) {
        return bookingSeatRepository.findAll((root, criteriaQuery, cb) -> {
            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(cb.equal(root.get("seat").get("id"), seatId));
            predicates.add(cb.equal(root.get("booking").get("trip").get("id"), tripId));
            predicates.add(cb.or(
                    cb.isFalse(root.get("isDeleted")),
                    cb.isNull(root.get("isDeleted"))));
            predicates.add(cb.or(
                    cb.isFalse(root.get("booking").get("isDeleted")),
                    cb.isNull(root.get("booking").get("isDeleted"))));
            predicates.add(cb.and(
                    cb.isNotNull(root.get("status")),
                    cb.notEqual(root.get("status"), "cancelled")));

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });
    }

    private String removeVietnameseAccents(String str) {
        if (str == null)
            return null;

        String result = str;
        result = result.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        result = result.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        result = result.replaceAll("[ìíịỉĩ]", "i");
        result = result.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        result = result.replaceAll("[ùúụủũưừứựửữ]", "u");
        result = result.replaceAll("[ỳýỵỷỹ]", "y");
        result = result.replaceAll("đ", "d");

        return result;
    }
}