package org.example.bookvexebej2e.services.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.BookingUserMapper;
import org.example.bookvexebej2e.models.constant.BookingStatus;
import org.example.bookvexebej2e.models.constant.SeatStatus;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSearchRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder; // ✅ Thêm encoder

    /**
     * Search booking by code, phone, or name
     */
    @Override
    public BookingResponse searchBooking(BookingSearchRequest searchRequest) {
        if ((searchRequest.getBookingCode() == null || searchRequest.getBookingCode().trim().isEmpty()) &&
                (searchRequest.getCustomerName() == null || searchRequest.getCustomerName().trim().isEmpty()) &&
                (searchRequest.getCustomerPhone() == null || searchRequest.getCustomerPhone().trim().isEmpty())) {
            throw new IllegalArgumentException("At least one search criterion must be provided");
        }

        BookingDbModel booking = null;

        // CASE 1: Search by booking code
        if (searchRequest.getBookingCode() != null && !searchRequest.getBookingCode().trim().isEmpty()) {
            booking = bookingUserRepository.findByCodeAndIsDeletedFalse(searchRequest.getBookingCode().trim())
                    .orElse(null);

            if (booking != null) {
                if (searchRequest.getCustomerName() != null &&
                        !searchRequest.getCustomerName().trim().isEmpty() &&
                        !booking.getCustomer().getName().equalsIgnoreCase(searchRequest.getCustomerName().trim())) {
                    throw new IllegalArgumentException(
                            "Booking not found: code matches but customer name does not match");
                }

                if (searchRequest.getCustomerPhone() != null &&
                        !searchRequest.getCustomerPhone().trim().isEmpty() &&
                        !booking.getCustomer().getPhone().equals(searchRequest.getCustomerPhone().trim())) {
                    throw new IllegalArgumentException(
                            "Booking not found: code matches but customer phone does not match");
                }

                return bookingUserMapper.toResponse(booking);
            } else {
                throw new IllegalArgumentException("Booking not found with code: " + searchRequest.getBookingCode());
            }
        }

        // CASE 2: Search by phone (and name optional)
        if (searchRequest.getCustomerPhone() != null && !searchRequest.getCustomerPhone().trim().isEmpty()) {
            List<BookingDbModel> bookingsByPhone = bookingUserRepository
                    .findByCustomer_PhoneAndIsDeletedFalse(searchRequest.getCustomerPhone().trim());

            if (!bookingsByPhone.isEmpty()) {
                if (searchRequest.getCustomerName() != null && !searchRequest.getCustomerName().trim().isEmpty()) {
                    booking = bookingsByPhone.stream()
                            .filter(b -> b.getCustomer().getName()
                                    .equalsIgnoreCase(searchRequest.getCustomerName().trim()))
                            .findFirst().orElse(null);

                    if (booking == null) {
                        throw new IllegalArgumentException("Booking not found with phone " +
                                searchRequest.getCustomerPhone() + " and name " + searchRequest.getCustomerName());
                    }
                } else {
                    booking = bookingsByPhone.get(0);
                }
                return bookingUserMapper.toResponse(booking);
            } else {
                throw new IllegalArgumentException("Booking not found with phone: " + searchRequest.getCustomerPhone());
            }
        }

        // CASE 3: Search by name only
        if (searchRequest.getCustomerName() != null && !searchRequest.getCustomerName().trim().isEmpty()) {
            List<BookingDbModel> bookingsByName = bookingUserRepository
                    .findByCustomer_NameContainingIgnoreCaseAndIsDeletedFalse(
                            searchRequest.getCustomerName().trim());

            if (!bookingsByName.isEmpty()) {
                booking = bookingsByName.get(0);
                return bookingUserMapper.toResponse(booking);
            } else {
                throw new IllegalArgumentException(
                        "Booking not found with customer name: " + searchRequest.getCustomerName());
            }
        }

        throw new IllegalArgumentException("Booking not found with provided search criteria");
    }

    /**
     * Create new booking (and create customer if needed)
     */
    @Transactional
    @Override
    public BookingResponse createBooking(BookingUserCreate createDto) {
        // 1. Find or create customer
        CustomerDbModel customer = findOrCreateCustomer(createDto);

        // 2. Validate trip and stops
        TripDbModel trip = tripRepository.findById(createDto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, createDto.getTripId()));

        TripStopDbModel pickupStop = tripStopRepository.findById(createDto.getPickupStopId())
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, createDto.getPickupStopId()));

        TripStopDbModel dropoffStop = tripStopRepository.findById(createDto.getDropoffStopId())
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, createDto.getDropoffStopId()));

        // 3. Create booking with AWAIT_PAYMENT status (seats are held, not booked yet)
        BookingDbModel booking = new BookingDbModel();
        booking.setCode(generateBookingCode());
        booking.setType(createDto.getType());
        booking.setCustomer(customer);
        booking.setTrip(trip);
        booking.setPickupStop(pickupStop);
        booking.setDropoffStop(dropoffStop);
        booking.setBookingStatus(BookingStatus.AWAIT_PAYMENT); // Change from NEW to AWAIT_PAYMENT
        booking.setTotalPrice(createDto.getTotalPrice());

        BookingDbModel savedBooking = bookingUserRepository.save(booking);

        // 4. Create booking seats with RESERVED status (not BOOKED yet)
        if (createDto.getBookingSeats() != null && !createDto.getBookingSeats().isEmpty()) {
            List<BookingSeatDbModel> bookingSeats = new ArrayList<>();
            for (BookingSeatCreate seatCreate : createDto.getBookingSeats()) {
                CarSeatDbModel seat = carSeatRepository.findById(seatCreate.getSeatId())
                        .orElseThrow(() -> new ResourceNotFoundException(CarSeatDbModel.class, seatCreate.getSeatId()));

                BookingSeatDbModel bookingSeat = new BookingSeatDbModel();
                bookingSeat.setCode(generateBookingSeatCode());
                bookingSeat.setBooking(savedBooking);
                bookingSeat.setSeat(seat);
                bookingSeat.setStatus(SeatStatus.RESERVED); // Use RESERVED instead of BOOKED
                bookingSeat.setPrice(seatCreate.getPrice());

                bookingSeats.add(bookingSeat);
            }
            bookingSeatRepository.saveAll(bookingSeats);
            savedBooking.setBookingSeats(bookingSeats);
        }

        return bookingUserMapper.toResponse(savedBooking);
    }

    /**
     * Get all bookings of the current customer (non-paged)
     */
    @Override
    public List<BookingResponse> getMyBookings() {
        CustomerDbModel customer = getCurrentCustomer();
        List<BookingDbModel> bookings = bookingUserRepository.findByCustomerAndIsDeletedFalse(customer);
        return bookings.stream().map(bookingUserMapper::toResponse).toList();
    }

    /**
     * Get paginated bookings of the current customer
     */
    @Override
    public Page<BookingResponse> getMyBookings(BookingQuery query) {
        CustomerDbModel customer = getCurrentCustomer();

        Specification<BookingDbModel> spec = (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("customer").get("id"), customer.getId()));
            predicates.add(cb.equal(root.get("isDeleted"), false));

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

        CustomerDbModel customer = getCurrentCustomer();
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("You don't have permission to access this booking");
        }

        return bookingUserMapper.toResponse(booking);
    }

    /**
     * Cancel booking if allowed
     */
    @Transactional
    @Override
    public BookingResponse cancelBooking(UUID id) {
        BookingDbModel booking = bookingUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        CustomerDbModel customer = getCurrentCustomer();
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("You don't have permission to cancel this booking");
        }

        if (BookingStatus.COMPLETED.equals(booking.getBookingStatus()) ||
                BookingStatus.CANCELLED.equals(booking.getBookingStatus())) {
            throw new IllegalStateException("Cannot cancel a booking that is already completed or cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        BookingDbModel updatedBooking = bookingUserRepository.save(booking);
        return bookingUserMapper.toResponse(updatedBooking);
    }

    /**
     * Confirm payment for booking
     */
    @Transactional
    @Override
    public BookingResponse confirmPayment(UUID id) {
        BookingDbModel booking = bookingUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        CustomerDbModel customer = getCurrentCustomer();
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("You don't have permission to access this booking");
        }

        // Only allow payment confirmation for AWAIT_PAYMENT status
        if (BookingStatus.AWAIT_PAYMENT.equals(booking.getBookingStatus())) {
            booking.setBookingStatus(BookingStatus.AWAIT_GO);

            // Update booking seats status from RESERVED to BOOKED
            if (booking.getBookingSeats() != null && !booking.getBookingSeats().isEmpty()) {
                for (BookingSeatDbModel bookingSeat : booking.getBookingSeats()) {
                    if (SeatStatus.RESERVED.equals(bookingSeat.getStatus())) {
                        bookingSeat.setStatus(SeatStatus.BOOKED);
                    }
                }
                bookingSeatRepository.saveAll(booking.getBookingSeats());
            }

            // Release seat holds for this booking since payment is confirmed
            // TODO: Integrate with SeatHoldService to release holds
        }

        BookingDbModel updatedBooking = bookingUserRepository.save(booking);
        return bookingUserMapper.toResponse(updatedBooking);
    }

    @Transactional
    @Override
    public BookingResponse confirmPaymentGuest(UUID id) {
        BookingDbModel booking = bookingUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        // Only allow payment confirmation for AWAIT_PAYMENT status
        if (BookingStatus.AWAIT_PAYMENT.equals(booking.getBookingStatus())) {
            booking.setBookingStatus(BookingStatus.AWAIT_GO);

            // Update booking seats status from RESERVED to BOOKED
            if (booking.getBookingSeats() != null && !booking.getBookingSeats().isEmpty()) {
                for (BookingSeatDbModel bookingSeat : booking.getBookingSeats()) {
                    if (SeatStatus.RESERVED.equals(bookingSeat.getStatus())) {
                        bookingSeat.setStatus(SeatStatus.BOOKED);
                    }
                }
                bookingSeatRepository.saveAll(booking.getBookingSeats());
            }
        }

        BookingDbModel updatedBooking = bookingUserRepository.save(booking);
        return bookingUserMapper.toResponse(updatedBooking);
    }

    // ========================= Helper methods =========================

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
        user.setPassword(passwordEncoder.encode("123456")); // ✅ Hash password
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
