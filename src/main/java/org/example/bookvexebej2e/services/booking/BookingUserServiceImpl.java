package org.example.bookvexebej2e.services.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.BookingUserMapper;
import org.example.bookvexebej2e.models.constant.BookingStatus;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingUserCreate;
import org.example.bookvexebej2e.repositories.booking.BookingSeatRepository;
import org.example.bookvexebej2e.repositories.booking.BookingUserRepository;
import org.example.bookvexebej2e.repositories.car.CarSeatRepository;
import org.example.bookvexebej2e.repositories.customer.CustomerRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.trip.TripStopRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingUserServiceImpl implements BookingUserService {

    private final BookingUserRepository bookingUserRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final TripStopRepository tripStopRepository;
    private final CarSeatRepository carSeatRepository;
    private final BookingUserMapper bookingUserMapper;

    @Transactional
    @Override
    public BookingResponse createBooking(BookingUserCreate createDto) {
        // 1. Tìm hoặc tạo Customer
        CustomerDbModel customer = findOrCreateCustomer(createDto);

        // 2. Validate Trip và Stops
        TripDbModel trip = tripRepository.findById(createDto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, createDto.getTripId()));

        TripStopDbModel pickupStop = tripStopRepository.findById(createDto.getPickupStopId())
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, createDto.getPickupStopId()));

        TripStopDbModel dropoffStop = tripStopRepository.findById(createDto.getDropoffStopId())
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, createDto.getDropoffStopId()));

        // 3. Tạo Booking
        BookingDbModel booking = new BookingDbModel();
        booking.setCode(generateBookingCode());
        booking.setType(createDto.getType());
        booking.setCustomer(customer);
        booking.setTrip(trip);
        booking.setPickupStop(pickupStop);
        booking.setDropoffStop(dropoffStop);
        booking.setBookingStatus(BookingStatus.NEW);
        booking.setTotalPrice(createDto.getTotalPrice());

        BookingDbModel savedBooking = bookingUserRepository.save(booking);

        // 4. Tạo BookingSeats
        if (createDto.getBookingSeats() != null && !createDto.getBookingSeats().isEmpty()) {
            List<BookingSeatDbModel> bookingSeats = new ArrayList<>();
            for (BookingSeatCreate seatCreate : createDto.getBookingSeats()) {
                CarSeatDbModel seat = carSeatRepository.findById(seatCreate.getSeatId())
                        .orElseThrow(() -> new ResourceNotFoundException(CarSeatDbModel.class, seatCreate.getSeatId()));

                BookingSeatDbModel bookingSeat = new BookingSeatDbModel();
                bookingSeat.setCode(generateBookingSeatCode());
                bookingSeat.setBooking(savedBooking);
                bookingSeat.setSeat(seat);
                bookingSeat.setStatus(seatCreate.getStatus());
                bookingSeat.setPrice(seatCreate.getPrice());

                bookingSeats.add(bookingSeat);
            }
            bookingSeatRepository.saveAll(bookingSeats);
            savedBooking.setBookingSeats(bookingSeats);
        }

        return bookingUserMapper.toResponse(savedBooking);
    }

    @Override
    public List<BookingResponse> getMyBookings() {
        CustomerDbModel customer = getCurrentCustomer();
        List<BookingDbModel> bookings = bookingUserRepository.findByCustomerAndIsDeletedFalse(customer);
        return bookings.stream()
                .map(bookingUserMapper::toResponse)
                .toList();
    }

    @Override
    public Page<BookingResponse> getMyBookings(BookingQuery query) {
        CustomerDbModel customer = getCurrentCustomer();

        Specification<BookingDbModel> spec = (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by current customer
            predicates.add(cb.equal(root.get("customer").get("id"), customer.getId()));
            predicates.add(cb.equal(root.get("isDeleted"), false));

            // Additional filters from query
            if (query.getCode() != null && !query.getCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + query.getCode().toLowerCase() + "%"));
            }
            if (query.getType() != null && !query.getType().isEmpty()) {
                predicates.add(cb.equal(root.get("type"), query.getType()));
            }
            if (query.getBookingStatus() != null && !query.getBookingStatus().isEmpty()) {
                predicates.add(cb.equal(root.get("bookingStatus"), query.getBookingStatus()));
            }
            if (query.getTripId() != null) {
                predicates.add(cb.equal(root.get("trip").get("id"), query.getTripId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = buildPageable(query);
        Page<BookingDbModel> bookings = bookingUserRepository.findAll(spec, pageable);
        return bookings.map(bookingUserMapper::toResponse);
    }

    @Override
    public BookingResponse getBookingById(UUID id) {
        BookingDbModel booking = bookingUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        // Verify the booking belongs to current customer
        CustomerDbModel customer = getCurrentCustomer();
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("You don't have permission to access this booking");
        }

        return bookingUserMapper.toResponse(booking);
    }

    @Transactional
    @Override
    public BookingResponse cancelBooking(UUID id) {
        BookingDbModel booking = bookingUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        // Verify the booking belongs to current customer
        CustomerDbModel customer = getCurrentCustomer();
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("You don't have permission to cancel this booking");
        }

        // Check if booking can be cancelled
        if (BookingStatus.COMPLETED.equals(booking.getBookingStatus()) ||
                BookingStatus.CANCELLED.equals(booking.getBookingStatus())) {
            throw new IllegalStateException("Cannot cancel a booking that is already completed or cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        BookingDbModel updatedBooking = bookingUserRepository.save(booking);

        return bookingUserMapper.toResponse(updatedBooking);
    }

    @Transactional
    @Override
    public BookingResponse confirmPayment(UUID id) {
        BookingDbModel booking = bookingUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        // Verify the booking belongs to current customer
        CustomerDbModel customer = getCurrentCustomer();
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("You don't have permission to access this booking");
        }

        // Update booking status after payment
        if (BookingStatus.NEW.equals(booking.getBookingStatus()) ||
                BookingStatus.AWAIT_PAYMENT.equals(booking.getBookingStatus())) {
            booking.setBookingStatus(BookingStatus.AWAIT_GO);
        }

        BookingDbModel updatedBooking = bookingUserRepository.save(booking);
        return bookingUserMapper.toResponse(updatedBooking);
    }

    // Helper methods

    private CustomerDbModel findOrCreateCustomer(BookingUserCreate createDto) {
        return customerRepository.findByPhoneAndIsDeletedFalse(createDto.getCustomerPhone())
                .orElseGet(() -> createNewCustomerAndUser(createDto));
    }

    @Transactional
    private CustomerDbModel createNewCustomerAndUser(BookingUserCreate createDto) {
        CustomerDbModel customer = new CustomerDbModel();
        customer.setCode(generateCustomerCode());
        customer.setName(createDto.getCustomerName());
        customer.setEmail(createDto.getCustomerEmail());
        customer.setPhone(createDto.getCustomerPhone());
        customer.setIsDeleted(false);

        CustomerDbModel savedCustomer = customerRepository.save(customer);

        UserDbModel user = new UserDbModel();
        user.setUsername(createDto.getCustomerPhone());
        user.setPassword("123456"); // Should be hashed in production
        user.setIsGoogle(false);
        user.setIsAdmin(false);
        user.setCustomer(savedCustomer);
        user.setIsDeleted(false);

        userRepository.save(user);

        return savedCustomer;
    }

    private CustomerDbModel getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDbModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, username));

        if (user.getCustomer() == null) {
            throw new IllegalStateException("User is not associated with a customer");
        }

        return user.getCustomer();
    }

    private Pageable buildPageable(BookingQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }

    private String generateBookingCode() {
        return "BK" + System.currentTimeMillis();
    }

    private String generateBookingSeatCode() {
        return "BS" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateCustomerCode() {
        return "CUS" + System.currentTimeMillis();
    }
}