package org.example.bookvexebej2e.services.user;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.*;
import org.example.bookvexebej2e.models.requests.BookingCreateRequest;
import org.example.bookvexebej2e.models.requests.BookingQueryRequest;
import org.example.bookvexebej2e.models.requests.BookingSeatCreateRequest;
import org.example.bookvexebej2e.models.responses.BookingResponse;
import org.example.bookvexebej2e.models.responses.BookingSeatResponse;
import org.example.bookvexebej2e.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingUserService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final CarSeatRepository carSeatRepository;

    /**
     * Tạo booking mới
     */
    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request, Jwt jwt) {
        // Get user from JWT
        String userEmail = jwt.getClaimAsString("email");
        UserDbModel user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // Override userId from request with actual user from JWT
        request.setUserId(user.getUserId());

        // Validate trip exists
        TripDbModel trip = tripRepository.findById(request.getTripId())
            .orElseThrow(() -> new RuntimeException("Trip not found with id: " + request.getTripId()));

        // Check if trip has available seats
        if (trip.getAvailableSeats() <= 0) {
            throw new RuntimeException("No available seats for this trip");
        }

        // Validate booking seats if provided
        if (!CollectionUtils.isEmpty(request.getBookingSeats())) {
            for (var seatRequest : request.getBookingSeats()) {
                CarSeatDbModel seat = carSeatRepository.findById(seatRequest.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatRequest.getSeatId()));

                // Check if seat is already booked for this trip
                boolean seatAlreadyBooked = bookingSeatRepository.findBySeat(seat)
                    .stream()
                    .anyMatch(bs -> bs.getBooking()
                        .getTrip()
                        .getTripId()
                        .equals(request.getTripId()) && bs.getIsReserved() && !"cancelled".equals(bs.getBooking()
                        .getBookingStatus()));

                if (seatAlreadyBooked) {
                    throw new RuntimeException("Seat " + seat.getSeatNumber() + " is already booked for this trip");
                }
            }
        }

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
                    .get();

                BookingSeatDbModel bookingSeat = new BookingSeatDbModel();
                bookingSeat.setBooking(booking);
                bookingSeat.setSeat(seat);
                bookingSeat.setPrice(seatRequest.getPrice());
                bookingSeat.setIsReserved(seatRequest.getIsReserved());

                bookingSeatRepository.save(bookingSeat);
            }

            // Update available seats in trip
            trip.setAvailableSeats(trip.getAvailableSeats() - request.getBookingSeats()
                .size());
            tripRepository.save(trip);
        }

        return convertToBookingResponse(booking);
    }

    /**
     * Lấy danh sách booking của user hiện tại
     */
    public Page<BookingResponse> getMyBookings(BookingQueryRequest queryRequest, Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");
        UserDbModel user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // Override userId in query to current user
        queryRequest.setUserId(user.getUserId());

        Specification<BookingDbModel> spec = buildSpecification(queryRequest);
        Pageable pageable = buildPageable(queryRequest);

        Page<BookingDbModel> bookingPage = bookingRepository.findAll(spec, pageable);
        return bookingPage.map(this::convertToBookingResponse);
    }

    /**
     * Lấy booking theo ID (chỉ booking của user hiện tại)
     */
    public BookingResponse getBookingById(Integer bookingId, Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");
        UserDbModel user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        // Check if booking belongs to current user
        if (!booking.getUser()
            .getUserId()
            .equals(user.getUserId())) {
            throw new RuntimeException("Access denied: Booking does not belong to current user");
        }

        return convertToBookingResponse(booking);
    }

    /**
     * Hủy booking
     */
    @Transactional
    public BookingResponse cancelBooking(Integer bookingId, Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");
        UserDbModel user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        // Check if booking belongs to current user
        if (!booking.getUser()
            .getUserId()
            .equals(user.getUserId())) {
            throw new RuntimeException("Access denied: Booking does not belong to current user");
        }

        // Check if booking can be cancelled
        if ("cancelled".equals(booking.getBookingStatus()) || "completed".equals(booking.getBookingStatus())) {
            throw new RuntimeException("Booking cannot be cancelled in current status: " + booking.getBookingStatus());
        }

        // Update booking status
        booking.setBookingStatus("cancelled");
        booking = bookingRepository.save(booking);

        // Free up the seats and update trip available seats
        List<BookingSeatDbModel> bookingSeats = bookingSeatRepository.findByBooking(booking);
        if (!bookingSeats.isEmpty()) {
            TripDbModel trip = booking.getTrip();
            trip.setAvailableSeats(trip.getAvailableSeats() + bookingSeats.size());
            tripRepository.save(trip);
        }

        return convertToBookingResponse(booking);
    }

    /**
     * Lấy booking theo trip ID (public)
     */
    public Page<BookingResponse> getBookingsByTripId(Integer tripId, BookingQueryRequest queryRequest) {
        queryRequest.setTripId(tripId);

        Specification<BookingDbModel> spec = buildSpecification(queryRequest);
        Pageable pageable = buildPageable(queryRequest);

        Page<BookingDbModel> bookingPage = bookingRepository.findAll(spec, pageable);
        return bookingPage.map(this::convertToBookingResponse);
    }

    /**
     * Build specification for filtering
     */
    private Specification<BookingDbModel> buildSpecification(BookingQueryRequest request) {
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
     * Build pageable from request
     */
    private Pageable buildPageable(BookingQueryRequest request) {
        return request.toPageable();
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
     * Thêm ghế vào booking của user hiện tại
     */
    @Transactional
    public BookingSeatResponse addSeatToMyBooking(Integer bookingId, BookingSeatCreateRequest request, Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");
        UserDbModel user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        // Check if booking belongs to current user
        if (!booking.getUser()
            .getUserId()
            .equals(user.getUserId())) {
            throw new RuntimeException("Access denied: Booking does not belong to current user");
        }

        // Check if booking can be modified
        if ("cancelled".equals(booking.getBookingStatus()) || "completed".equals(booking.getBookingStatus())) {
            throw new RuntimeException("Cannot modify booking in current status: " + booking.getBookingStatus());
        }

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
     * Xóa ghế khỏi booking của user hiện tại
     */
    @Transactional
    public void removeSeatFromMyBooking(Integer bookingId, Integer seatId, Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");
        UserDbModel user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        BookingDbModel booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        // Check if booking belongs to current user
        if (!booking.getUser()
            .getUserId()
            .equals(user.getUserId())) {
            throw new RuntimeException("Access denied: Booking does not belong to current user");
        }

        // Check if booking can be modified
        if ("cancelled".equals(booking.getBookingStatus()) || "completed".equals(booking.getBookingStatus())) {
            throw new RuntimeException("Cannot modify booking in current status: " + booking.getBookingStatus());
        }

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
}