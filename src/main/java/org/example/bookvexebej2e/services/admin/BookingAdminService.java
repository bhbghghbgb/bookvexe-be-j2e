package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.*;
import org.example.bookvexebej2e.models.requests.BookingCreateRequest;
import org.example.bookvexebej2e.models.requests.BookingQueryRequest;
import org.example.bookvexebej2e.models.requests.BookingSeatCreateRequest;
import org.example.bookvexebej2e.models.requests.BookingUpdateRequest;
import org.example.bookvexebej2e.models.responses.BookingResponse;
import org.example.bookvexebej2e.models.responses.BookingSeatResponse;
import org.example.bookvexebej2e.repositories.*;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingAdminService extends BaseAdminService<BookingDbModel, Integer, BookingQueryRequest> {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final CarSeatRepository carSeatRepository;

    @Override
    protected JpaRepository<BookingDbModel, Integer> getRepository() {
        return bookingRepository;
    }

    @Override
    protected Specification<BookingDbModel> buildSpecification(BookingQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by user ID
            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("user")
                    .get("userId"), request.getUserId()));
            }

            // Filter by trip ID
            if (request.getTripId() != null) {
                predicates.add(cb.equal(root.get("trip")
                    .get("tripId"), request.getTripId()));
            }

            // Filter by single status
            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("bookingStatus"), request.getStatus()));
            }

            // Filter by multiple statuses
            if (!CollectionUtils.isEmpty(request.getStatuses())) {
                predicates.add(root.get("bookingStatus")
                    .in(request.getStatuses()));
            }

            // Filter by created date range
            if (request.getCreatedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getCreatedAfter()));
            }

            if (request.getCreatedBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getCreatedBefore()));
            }

            // Filter by price range
            if (request.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalPrice"), request.getMinPrice()));
            }

            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalPrice"), request.getMaxPrice()));
            }

            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }

    /**
     * Lấy danh sách ghế của booking
     */
    public List<BookingSeatDbModel> getBookingSeats(Integer bookingId) {
        return bookingRepository.findById(bookingId)
            .map(bookingSeatRepository::findByBooking)
            .orElse(Collections.emptyList());
    }

    /**
     * Tạo mới booking
     */
    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        // Validate user exists
        UserDbModel user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // Validate trip exists
        TripDbModel trip = tripRepository.findById(request.getTripId())
            .orElseThrow(() -> new RuntimeException("Trip not found with id: " + request.getTripId()));

        // Create booking entity
        BookingDbModel booking = new BookingDbModel();
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setBookingStatus(request.getBookingStatus());
        booking.setTotalPrice(request.getTotalPrice());

        // Save booking
        booking = bookingRepository.save(booking);

        // Create booking seats if provided
        if (!CollectionUtils.isEmpty(request.getBookingSeats())) {
            for (var seatRequest : request.getBookingSeats()) {
                CarSeatDbModel seat = carSeatRepository.findById(seatRequest.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatRequest.getSeatId()));

                BookingSeatDbModel bookingSeat = new BookingSeatDbModel();
                bookingSeat.setBooking(booking);
                bookingSeat.setSeat(seat);
                bookingSeat.setPrice(seatRequest.getPrice());
                bookingSeat.setIsReserved(seatRequest.getIsReserved());

                bookingSeatRepository.save(bookingSeat);
            }
        }

        return convertToBookingResponse(booking);
    }

    /**
     * Cập nhật booking
     */
    @Transactional
    public BookingResponse updateBooking(Integer bookingId, BookingUpdateRequest request) {
        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        // Update fields if provided
        if (request.getUserId() != null) {
            UserDbModel user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
            booking.setUser(user);
        }

        if (request.getTripId() != null) {
            TripDbModel trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + request.getTripId()));
            booking.setTrip(trip);
        }

        if (request.getBookingStatus() != null) {
            booking.setBookingStatus(request.getBookingStatus());
        }

        if (request.getTotalPrice() != null) {
            booking.setTotalPrice(request.getTotalPrice());
        }

        booking = bookingRepository.save(booking);
        return convertToBookingResponse(booking);
    }

    /**
     * Cập nhật trạng thái booking
     */
    @Transactional
    public BookingResponse updateBookingStatus(Integer bookingId, String status) {
        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        booking.setBookingStatus(status);
        booking = bookingRepository.save(booking);
        return convertToBookingResponse(booking);
    }

    /**
     * Convert BookingDbModel to BookingResponse
     */
    private BookingResponse convertToBookingResponse(BookingDbModel booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setUserId(booking.getUser()
            .getUserId());
        response.setUserFullName(booking.getUser()
            .getFullName());
        response.setUserEmail(booking.getUser()
            .getEmail());
        response.setTripId(booking.getTrip()
            .getTripId());

        if (booking.getTrip()
            .getRoute() != null) {
            response.setRouteStartLocation(booking.getTrip()
                .getRoute()
                .getStartLocation());
            response.setRouteEndLocation(booking.getTrip()
                .getRoute()
                .getEndLocation());
        }

        response.setDepartureTime(booking.getTrip()
            .getDepartureTime());
        response.setBookingStatus(booking.getBookingStatus());
        response.setTotalPrice(booking.getTotalPrice());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());

        // Get booking seats
        List<BookingSeatDbModel> bookingSeats = bookingSeatRepository.findByBooking(booking);
        List<BookingSeatResponse> seatResponses = bookingSeats.stream()
            .map(this::convertToBookingSeatResponse)
            .collect(Collectors.toList());
        response.setBookingSeats(seatResponses);

        return response;
    }

    /**
     * Convert BookingSeatDbModel to BookingSeatResponse
     */
    private BookingSeatResponse convertToBookingSeatResponse(BookingSeatDbModel bookingSeat) {
        BookingSeatResponse response = new BookingSeatResponse();
        response.setBookingSeatId(bookingSeat.getBookingSeatId());
        response.setBookingId(bookingSeat.getBooking()
            .getBookingId());
        response.setSeatId(bookingSeat.getSeat()
            .getSeatId());
        response.setSeatNumber(bookingSeat.getSeat()
            .getSeatNumber());
        response.setSeatPosition(bookingSeat.getSeat()
            .getSeatPosition());
        response.setIsReserved(bookingSeat.getIsReserved());
        response.setPrice(bookingSeat.getPrice());
        return response;
    }

    /**
     * Thêm ghế vào booking
     */
    @Transactional
    public BookingSeatResponse addSeatToBooking(Integer bookingId, BookingSeatCreateRequest request) {
        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        CarSeatDbModel seat = carSeatRepository.findById(request.getSeatId())
            .orElseThrow(() -> new RuntimeException("Seat not found with id: " + request.getSeatId()));

        // Check if seat is already booked for this trip
        boolean seatAlreadyBooked = bookingSeatRepository.findBySeat(seat)
            .stream()
            .anyMatch(bs -> bs.getBooking()
                .getTrip()
                .getTripId()
                .equals(booking.getTrip()
                    .getTripId()) && bs.getIsReserved() && !"cancelled".equals(bs.getBooking()
                .getBookingStatus()) && !bs.getBooking()
                .getBookingId()
                .equals(bookingId));

        if (seatAlreadyBooked) {
            throw new RuntimeException("Seat " + seat.getSeatNumber() + " is already booked for this trip");
        }

        BookingSeatDbModel bookingSeat = new BookingSeatDbModel();
        bookingSeat.setBooking(booking);
        bookingSeat.setSeat(seat);
        bookingSeat.setPrice(request.getPrice());
        bookingSeat.setIsReserved(request.getIsReserved());

        bookingSeat = bookingSeatRepository.save(bookingSeat);

        // Update trip available seats if seat is reserved
        if (request.getIsReserved()) {
            TripDbModel trip = booking.getTrip();
            trip.setAvailableSeats(trip.getAvailableSeats() - 1);
            tripRepository.save(trip);

            // Update booking total price
            booking.setTotalPrice(booking.getTotalPrice()
                .add(request.getPrice()));
            bookingRepository.save(booking);
        }

        return convertToBookingSeatResponse(bookingSeat);
    }

    /**
     * Xóa ghế khỏi booking
     */
    @Transactional
    public void removeSeatFromBooking(Integer bookingId, Integer seatId) {
        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        BookingSeatDbModel bookingSeat = bookingSeatRepository.findByBooking(booking)
            .stream()
            .filter(bs -> bs.getSeat()
                .getSeatId()
                .equals(seatId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Seat not found in this booking"));

        // Update trip available seats if seat was reserved
        if (bookingSeat.getIsReserved()) {
            TripDbModel trip = booking.getTrip();
            trip.setAvailableSeats(trip.getAvailableSeats() + 1);
            tripRepository.save(trip);

            // Update booking total price
            booking.setTotalPrice(booking.getTotalPrice()
                .subtract(bookingSeat.getPrice()));
            bookingRepository.save(booking);
        }

        bookingSeatRepository.delete(bookingSeat);
    }

    /**
     * Cập nhật thông tin ghế booking
     */
    @Transactional
    public BookingSeatResponse updateBookingSeat(Integer bookingId, Integer bookingSeatId,
        BookingSeatCreateRequest request) {
        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        BookingSeatDbModel bookingSeat = bookingSeatRepository.findById(bookingSeatId)
            .orElseThrow(() -> new RuntimeException("Booking seat not found with id: " + bookingSeatId));

        // Verify booking seat belongs to the booking
        if (!bookingSeat.getBooking()
            .getBookingId()
            .equals(bookingId)) {
            throw new RuntimeException("Booking seat does not belong to this booking");
        }

        boolean wasReserved = bookingSeat.getIsReserved();
        java.math.BigDecimal oldPrice = bookingSeat.getPrice();

        // Update seat if provided
        if (request.getSeatId() != null && !request.getSeatId()
            .equals(bookingSeat.getSeat()
                .getSeatId())) {
            CarSeatDbModel newSeat = carSeatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + request.getSeatId()));

            // Check if new seat is already booked for this trip
            boolean seatAlreadyBooked = bookingSeatRepository.findBySeat(newSeat)
                .stream()
                .anyMatch(bs -> bs.getBooking()
                    .getTrip()
                    .getTripId()
                    .equals(booking.getTrip()
                        .getTripId()) && bs.getIsReserved() && !"cancelled".equals(bs.getBooking()
                    .getBookingStatus()) && !bs.getBookingSeatId()
                    .equals(bookingSeatId));

            if (seatAlreadyBooked) {
                throw new RuntimeException("Seat " + newSeat.getSeatNumber() + " is already booked for this trip");
            }

            bookingSeat.setSeat(newSeat);
        }

        // Update price
        if (request.getPrice() != null) {
            bookingSeat.setPrice(request.getPrice());
        }

        // Update reservation status
        if (request.getIsReserved() != null) {
            bookingSeat.setIsReserved(request.getIsReserved());
        }

        bookingSeat = bookingSeatRepository.save(bookingSeat);

        // Update trip available seats and booking total price
        TripDbModel trip = booking.getTrip();
        java.math.BigDecimal priceDifference = bookingSeat.getPrice()
            .subtract(oldPrice);

        if (wasReserved && !bookingSeat.getIsReserved()) {
            // Seat was unreserved
            trip.setAvailableSeats(trip.getAvailableSeats() + 1);
            booking.setTotalPrice(booking.getTotalPrice()
                .subtract(oldPrice));
        } else if (!wasReserved && bookingSeat.getIsReserved()) {
            // Seat was reserved
            trip.setAvailableSeats(trip.getAvailableSeats() - 1);
            booking.setTotalPrice(booking.getTotalPrice()
                .add(bookingSeat.getPrice()));
        } else if (wasReserved && bookingSeat.getIsReserved()) {
            // Seat remained reserved, update price difference
            booking.setTotalPrice(booking.getTotalPrice()
                .add(priceDifference));
        }

        tripRepository.save(trip);
        bookingRepository.save(booking);

        return convertToBookingSeatResponse(bookingSeat);
    }
}
