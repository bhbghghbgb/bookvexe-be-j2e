package org.example.bookvexebej2e.services.booking;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.configs.SecurityUtils;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.BookingUserMapper;
import org.example.bookvexebej2e.models.constant.BookingStatus;
import org.example.bookvexebej2e.models.constant.SeatStatus;
import org.example.bookvexebej2e.models.db.*;
import org.example.bookvexebej2e.models.dto.booking.*;
import org.example.bookvexebej2e.repositories.booking.BookingSeatRepository;
import org.example.bookvexebej2e.repositories.booking.BookingUserRepository;
import org.example.bookvexebej2e.repositories.car.CarSeatRepository;
import org.example.bookvexebej2e.repositories.customer.CustomerRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.trip.TripStopRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.example.bookvexebej2e.services.notification.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingUserServiceImpl implements BookingUserService {

    private final BookingUserRepository bookingUserRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final TripStopRepository tripStopRepository;
    private final CarSeatRepository carSeatRepository;
    private final NotificationService notificationService;
    private final BookingUserMapper bookingUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils security;
    private final org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;
    private final org.example.bookvexebej2e.services.seat.SeatHoldService seatHoldService;

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
                            "Không tìm được vé đặt xe: Tên khách hàng không đúng");
                }

                if (searchRequest.getCustomerPhone() != null &&
                        !searchRequest.getCustomerPhone().trim().isEmpty() &&
                        !booking.getCustomer().getPhone().equals(searchRequest.getCustomerPhone().trim())) {
                    throw new IllegalArgumentException(
                            "Không tìm được vé đặt xe: Số điện thoại khách hàng không đúng");
                }

                return bookingUserMapper.toResponse(booking);
            } else {
                throw new IllegalArgumentException(
                        "Không tìm thấy vé đặt xe với mã vé: " + searchRequest.getBookingCode());
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
                        throw new IllegalArgumentException("Không tìm thấy vé đặt xe với số điện thoại " +
                                searchRequest.getCustomerPhone() + " và tên " + searchRequest.getCustomerName());
                    }
                } else {
                    booking = bookingsByPhone.get(0);
                }
                return bookingUserMapper.toResponse(booking);
            } else {
                throw new IllegalArgumentException(
                        "Không tìm thấy vé đặt xe với số điện thoại: " + searchRequest.getCustomerPhone());
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
                        "Không tìm thấy vé đặt xe với tên khách hàng: " + searchRequest.getCustomerName());
            }
        }

        throw new IllegalArgumentException("Không tìm thấy đặt chỗ theo các thông tin trên");
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

        // 3. Create booking with appropriate status based on payment type
        BookingDbModel booking = new BookingDbModel();
        booking.setCode(generateBookingCode());
        booking.setType(createDto.getType());
        booking.setCustomer(customer);
        booking.setTrip(trip);
        booking.setPickupStop(pickupStop);
        booking.setDropoffStop(dropoffStop);

        // Cash payments are immediately confirmed, others await payment
        if ("CASH".equalsIgnoreCase(createDto.getType())) {
            booking.setBookingStatus(BookingStatus.AWAIT_GO); // Cash payments are confirmed immediately
        } else {
            booking.setBookingStatus(BookingStatus.AWAIT_PAYMENT); // Online payments need confirmation
        }

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

                // Set seat status based on payment type
                if ("CASH".equalsIgnoreCase(createDto.getType())) {
                    bookingSeat.setStatus(SeatStatus.BOOKED); // Cash payments - seats are immediately booked
                } else {
                    bookingSeat.setStatus(SeatStatus.RESERVED); // Online payments - seats are reserved until payment
                }

                bookingSeat.setPrice(seatCreate.getPrice());

                bookingSeats.add(bookingSeat);
            }
            bookingSeatRepository.saveAll(bookingSeats);
            savedBooking.setBookingSeats(bookingSeats);
        }

        // Broadcast seat status changes for cash payments (seats are immediately
        // BOOKED)
        if ("CASH".equalsIgnoreCase(createDto.getType()) && createDto.getBookingSeats() != null
                && !createDto.getBookingSeats().isEmpty()) {
            try {
                List<String> seatIds = createDto.getBookingSeats().stream()
                        .map(seat -> seat.getSeatId().toString())
                        .collect(java.util.stream.Collectors.toList());

                // Get car ID from the first seat (all seats should be from the same car)
                CarSeatDbModel firstSeat = carSeatRepository.findById(createDto.getBookingSeats().get(0).getSeatId())
                        .orElse(null);

                if (firstSeat != null) {
                    // Broadcast that these seats are now BOOKED (not just reserved)
                    org.example.bookvexebej2e.models.dto.seat.SeatUpdatePayload payload = new org.example.bookvexebej2e.models.dto.seat.SeatUpdatePayload();
                    payload.setTripId(createDto.getTripId().toString());
                    payload.setCarId(firstSeat.getCar().getId().toString());
                    payload.setSeatIds(seatIds);
                    payload.setAction("book"); // Custom action for booked seats
                    payload.setBy("CASH_PAYMENT");

                    // Use messaging template to broadcast
                    String topic = "/topic/seats/" + payload.getTripId() + "/" + payload.getCarId();
                    messagingTemplate.convertAndSend(topic, payload);
                }
            } catch (Exception e) {
            }
        }

        // ADD NOTIFICATION: Booking Created
        try {
            // Determine if this is a guest booking (no authenticated user)
            CustomerDbModel customer2 = savedBooking.getCustomer();
            UUID userId = Optional.ofNullable(security.getCurrentUserEntity())
                    .map(UserDbModel::getId)
                    .orElse(null);
            String customerEmail = customer2.getEmail();

            if (userId != null && "CASH".equalsIgnoreCase(createDto.getType())) {
                // Registered user - can save notification and send WebSocket
                notificationService.sendNotification(
                        userId,
                        "TYPE_BOOKING_CREATED",
                        "Đặt vé thành công",
                        "Bạn đã đặt vé thành công. Mã đặt vé: " + savedBooking.getCode() +
                                ". Vui lòng thanh toán tiền mặt để hoàn tất.",
                        savedBooking.getId(),
                        savedBooking.getTrip().getId(),
                        "APP",
                        true, // sendEmail
                        true // shouldSave
                );
            } else {
                // Guest user - can only send email
                notificationService.sendGuestNotification(
                        customerEmail,
                        "TYPE_BOOKING_CREATED",
                        "Đặt vé thành công",
                        "Bạn đã đặt vé thành công. Mã đặt vé: " + savedBooking.getCode() +
                                ". Vui lòng thanh toán tiền mặt để hoàn tất.",
                        savedBooking.getId(),
                        savedBooking.getTrip().getId(),
                        "EMAIL",
                        true, // sendEmail
                        false // shouldSave - cannot save without user
                );
            }
        } catch (Exception e) {
            log.error("Failed to send booking creation notification: {}", e.getMessage(),
                    e);
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
            throw new IllegalArgumentException("You don't have permission to access this booking");
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
            throw new IllegalArgumentException("Bạn không có quyền hủy đặt xe này");
        }

        if (BookingStatus.COMPLETED.equals(booking.getBookingStatus()) ||
                BookingStatus.CANCELLED.equals(booking.getBookingStatus())) {
            throw new IllegalStateException("Không thể hủy đặt chỗ đã hoàn tất hoặc đã hủy");
        }

        return performBookingCancellation(booking);
    }

    /**
     * Cancel booking for guest users (no authentication required)
     */
    @Transactional
    public BookingResponse cancelBookingGuest(UUID id) {
        BookingDbModel booking = bookingUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        if (BookingStatus.COMPLETED.equals(booking.getBookingStatus()) ||
                BookingStatus.CANCELLED.equals(booking.getBookingStatus())) {
            throw new IllegalStateException("Không thể hủy đặt chỗ đã hoàn tất hoặc đã hủy");
        }

        return performBookingCancellation(booking);
    }

    /**
     * Common logic for booking cancellation with seat hold release and realtime
     * broadcast
     */
    private BookingResponse performBookingCancellation(BookingDbModel booking) {
        try {
            // 1. Update booking status
            booking.setBookingStatus(BookingStatus.CANCELLED);
            BookingDbModel updatedBooking = bookingUserRepository.save(booking);

            // 2. Release seat holds and broadcast realtime updates
            if (updatedBooking.getBookingSeats() != null && !updatedBooking.getBookingSeats().isEmpty()) {
                // Get trip info
                UUID tripId = booking.getTrip().getId();

                // Extract seat IDs and find car ID from first seat
                List<String> seatIdStrings = new ArrayList<>();
                UUID carId = null;

                for (BookingSeatDbModel bookingSeat : updatedBooking.getBookingSeats()) {
                    if (!bookingSeat.getIsDeleted()) {
                        seatIdStrings.add(bookingSeat.getSeat().getId().toString());
                        if (carId == null) {
                            carId = bookingSeat.getSeat().getCar().getId();
                        }
                    }
                }

                if (!seatIdStrings.isEmpty() && carId != null) {
                    // Create SeatHoldRequest to use existing release method
                    org.example.bookvexebej2e.models.dto.seat.SeatHoldRequest releaseRequest = new org.example.bookvexebej2e.models.dto.seat.SeatHoldRequest();
                    releaseRequest.setTripId(tripId.toString());
                    releaseRequest.setCarId(carId.toString());
                    releaseRequest.setSeatIds(seatIdStrings);

                    // Release seat holds using existing method
                    boolean released = seatHoldService.releaseSeats(releaseRequest, "booking_cancelled", null);

                    // Also broadcast seat update specifically for booking cancellation
                    seatHoldService.broadcastSeatUpdate(
                            tripId.toString(),
                            carId.toString(),
                            seatIdStrings,
                            "release",
                            null,
                            "booking_cancelled");

                    log.info("Released {} seats for cancelled booking: {} (released: {})",
                            seatIdStrings.size(), booking.getCode(), released);
                }
            }

            return bookingUserMapper.toResponse(updatedBooking);
        } catch (Exception e) {
            log.error("Error cancelling booking {}: {}", booking.getId(), e.getMessage(), e);
            throw new RuntimeException("Có lỗi khi hủy đặt vé: " + e.getMessage());
        }
    }

    // ========================= Helper methods =========================

    private CustomerDbModel findOrCreateCustomer(BookingUserCreate createDto) {
        return customerRepository.findByPhoneAndIsDeletedFalse(createDto.getCustomerPhone())
                .orElseGet(() -> createNewCustomerAndUser(createDto));
    }

    private CustomerDbModel createNewCustomerAndUser(BookingUserCreate createDto) {
        CustomerDbModel customer = new CustomerDbModel();
        customer.setCode(generateCustomerCode());
        customer.setName(createDto.getCustomerName());
        customer.setEmail(createDto.getCustomerEmail());
        customer.setPhone(createDto.getCustomerPhone());
        customer.setIsDeleted(false);

        CustomerDbModel savedCustomer = customerRepository.save(customer);

        // Check if user with this username already exists
        if (userRepository.findByUsername(createDto.getCustomerPhone()).isEmpty()) {
            UserDbModel user = new UserDbModel();
            user.setUsername(createDto.getCustomerPhone());
            user.setPassword(passwordEncoder.encode("123456")); // ✅ Hash password
            user.setIsGoogle(false);
            user.setIsAdmin(false);
            user.setCustomer(savedCustomer);
            user.setIsDeleted(false);

            userRepository.save(user);
        } else {
            log.warn("User with username {} already exists, skipping user creation for new customer", createDto.getCustomerPhone());
        }

        return savedCustomer;
    }

    private CustomerDbModel getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDbModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, username));

        if (user.getCustomer() == null) {
            throw new IllegalStateException("Người dùng không liên kết với khách hàng");
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
