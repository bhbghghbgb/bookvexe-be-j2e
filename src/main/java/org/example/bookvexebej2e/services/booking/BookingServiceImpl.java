package org.example.bookvexebej2e.services.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.helpers.api.PaymentClient;
import org.example.bookvexebej2e.mappers.BookingMapper;
import org.example.bookvexebej2e.models.constant.BookingStatus;
import org.example.bookvexebej2e.models.constant.SeatStatus;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingSelectResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingUpdate;
import org.example.bookvexebej2e.repositories.booking.BookingRepository;
import org.example.bookvexebej2e.repositories.booking.BookingSeatRepository;
import org.example.bookvexebej2e.repositories.customer.CustomerRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.trip.TripStopRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.example.bookvexebej2e.services.notification.NotificationService;
import org.example.bookvexebej2e.services.seat.SeatHoldService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Booking Service Implementation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final TripRepository tripRepository;
    private final TripStopRepository tripStopRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final BookingSeatService bookingSeatService;
    private final BookingMapper bookingMapper;
    private final PaymentClient paymentClient;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatHoldService seatHoldService;

    @Override
    public List<BookingResponse> findAll() {
        List<BookingDbModel> entities = bookingRepository.findAllNotDeleted();
        return entities.stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public Page<BookingResponse> findAll(BookingQuery query) {
        Specification<BookingDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<BookingDbModel> entities = bookingRepository.findAll(spec, pageable);

        // Auto-update status based on departure date for eligible bookings
        LocalDateTime now = LocalDateTime.now();
        entities.forEach(entity -> {
            if (BookingStatus.AWAIT_GO.equals(entity.getBookingStatus()) &&
                    entity.getTrip() != null &&
                    entity.getTrip().getDepartureTime() != null) {

                LocalDateTime departureTime = entity.getTrip().getDepartureTime();
                if (now.isAfter(departureTime) || now.isEqual(departureTime)) {
                    entity.setBookingStatus(BookingStatus.DEPARTING);
                    bookingRepository.save(entity);
                }
            }
        });

        return entities.map(bookingMapper::toResponse);
    }

    @Override
    public BookingResponse findById(UUID id) {
        BookingDbModel entity = bookingRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));
        return bookingMapper.toResponse(entity);
    }

    @Override
    public BookingResponse create(BookingCreate createDto) {
        BookingDbModel entity = new BookingDbModel();
        entity.setCode(createDto.getCode());
        entity.setType(createDto.getType());

        if (createDto.getCode() != null && !createDto.getCode().isEmpty()) {
            bookingRepository.findByCode(createDto.getCode()).ifPresent(existingCarType -> {
                throw new IllegalStateException(
                        "Không thể tạo đặt xe vì mã đặt xe '" + createDto.getCode() + "' đã tồn tại trong hệ thống");
            });
        }

        // For admin bookings with payment confirmed, set status directly to AWAIT_GO
        // For user bookings, they should go through AWAIT_PAYMENT -> AWAIT_GO flow
        if (createDto.getBookingStatus() != null) {
            entity.setBookingStatus(createDto.getBookingStatus());
        } else {
            // Default for admin created bookings with payment is AWAIT_GO
            entity.setBookingStatus(BookingStatus.AWAIT_GO);
        }

        entity.setTotalPrice(createDto.getTotalPrice());

        // Resolve relationships
        CustomerDbModel customer = customerRepository.findById(createDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, createDto.getCustomerId()));
        entity.setCustomer(customer);

        TripDbModel trip = tripRepository.findById(createDto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, createDto.getTripId()));
        entity.setTrip(trip);

        TripStopDbModel pickupStop = tripStopRepository.findById(createDto.getPickupStopId())
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, createDto.getPickupStopId()));
        entity.setPickupStop(pickupStop);

        TripStopDbModel dropoffStop = tripStopRepository.findById(createDto.getDropoffStopId())
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, createDto.getDropoffStopId()));
        entity.setDropoffStop(dropoffStop);

        BookingDbModel savedEntity = bookingRepository.save(entity);

        // Create booking seats via service
        if (createDto.getBookingSeats() != null) {
            for (BookingSeatCreate seatDto : createDto.getBookingSeats()) {
                seatDto.setBookingId(savedEntity.getId()); // Inject booking ID
                bookingSeatService.create(seatDto); // Reuse service logic
            }
        }

        // Send notification for new booking
        try {
            UUID userId = userRepository.findByCustomerId(savedEntity.getCustomer()
                    .getId())
                    .orElseThrow().getId();
            String message = String.format("Đặt chỗ %s đã được tạo thành công. Trạng thái: %s",
                    savedEntity.getCode(), savedEntity.getBookingStatus());

            notificationService.sendNotification(
                    userId,
                    "TYPE_BOOKING_CREATED",
                    "Đặt chỗ mới",
                    message,
                    savedEntity.getId(),
                    savedEntity.getTrip().getId(),
                    "CHANNEL_BOOKING",
                    true, // sendEmail
                    true // shouldSave
            );
        } catch (Exception e) {
            log.warn("Failed to send notification for booking creation: {}", e.getMessage());
        }

        return bookingMapper.toResponse(savedEntity);
    }

    @Override
    public BookingResponse update(UUID id, BookingUpdate updateDto) {
        BookingDbModel entity = bookingRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        // Check for duplicate code (excluding current entity)
        if (updateDto.getCode() != null && !updateDto.getCode().isEmpty()) {
            bookingRepository.findByCode(updateDto.getCode()).ifPresent(existingCarType -> {
                if (!existingCarType.getId().equals(id)) {
                    throw new IllegalStateException("Không thể cập nhật đặt xe vì mã đặt xe '" + updateDto.getCode()
                            + "' đã tồn tại trong hệ thống");
                }
            });
        }

        entity.setCode(updateDto.getCode());
        entity.setType(updateDto.getType());
        entity.setBookingStatus(updateDto.getBookingStatus());
        entity.setTotalPrice(updateDto.getTotalPrice());

        // Resolve relationships if provided
        if (updateDto.getCustomerId() != null) {
            CustomerDbModel customer = customerRepository.findById(updateDto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, updateDto.getCustomerId()));
            entity.setCustomer(customer);
        }

        if (updateDto.getTripId() != null) {
            TripDbModel trip = tripRepository.findById(updateDto.getTripId())
                    .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, updateDto.getTripId()));
            entity.setTrip(trip);
        }

        if (updateDto.getPickupStopId() != null) {
            TripStopDbModel pickupStop = tripStopRepository.findById(updateDto.getPickupStopId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(TripStopDbModel.class, updateDto.getPickupStopId()));
            entity.setPickupStop(pickupStop);
        }

        if (updateDto.getDropoffStopId() != null) {
            TripStopDbModel dropoffStop = tripStopRepository.findById(updateDto.getDropoffStopId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(TripStopDbModel.class, updateDto.getDropoffStopId()));
            entity.setDropoffStop(dropoffStop);
        }

        BookingDbModel updatedEntity = bookingRepository.save(entity);
        return bookingMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        bookingRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        BookingDbModel entity = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));
        entity.setIsDeleted(false);
        bookingRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        BookingDbModel entity = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));
        entity.setIsDeleted(true);
        bookingRepository.save(entity);
    }

    @Override
    public List<BookingSelectResponse> findAllForSelect() {
        List<BookingDbModel> entities = bookingRepository.findAllNotDeletedWithCustomer();
        return entities.stream()
                .map(bookingMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<BookingSelectResponse> findAllForSelect(BookingQuery query) {
        Specification<BookingDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<BookingDbModel> entities = bookingRepository.findAll(spec, pageable);
        return entities.map(bookingMapper::toSelectResponse);
    }

    private Specification<BookingDbModel> buildSpecification(BookingQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Remove the isDeleted filter to show all records including deleted ones

            if (query.getCode() != null && !query.getCode()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + query.getCode()
                        .toLowerCase() + "%"));
            }
            if (query.getType() != null && !query.getType()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("type")), "%" + query.getType()
                        .toLowerCase() + "%"));
            }
            if (query.getBookingStatus() != null && !query.getBookingStatus()
                    .isEmpty()) {
                predicates.add(cb.equal(root.get("bookingStatus"), query.getBookingStatus()));
            }
            if (query.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer")
                        .get("id"), query.getCustomerId()));
            }
            if (query.getTripId() != null) {
                predicates.add(cb.equal(root.get("trip")
                        .get("id"), query.getTripId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(BookingQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }

    @Override
    public BookingResponse confirmTrip(UUID id) {
        BookingDbModel entity = bookingRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        // Validate current status and confirm booking
        if (!(BookingStatus.NEW.equals(entity.getBookingStatus())
                || BookingStatus.AWAIT_PAYMENT.equals(entity.getBookingStatus()))) {
            throw new IllegalStateException(
                    "Booking can only be confirmed when status is 'new' or 'await_payment'. Current status: "
                            + entity.getBookingStatus());
        }

        // Update all related booking seats from RESERVED to BOOKED so that
        // they are treated as permanently sold seats
        if (entity.getBookingSeats() != null && !entity.getBookingSeats().isEmpty()) {
            for (BookingSeatDbModel bookingSeat : entity.getBookingSeats()) {
                if (SeatStatus.RESERVED.equals(bookingSeat.getStatus())) {
                    bookingSeat.setStatus(SeatStatus.BOOKED);
                }
            }
            bookingSeatRepository.saveAll(entity.getBookingSeats());
        }

        entity.setBookingStatus(BookingStatus.AWAIT_GO);

        BookingDbModel savedEntity = bookingRepository.save(entity);

        // Release seat holds after successful confirmation
        try {
            if (savedEntity.getBookingSeats() != null && !savedEntity.getBookingSeats().isEmpty()) {
                UUID tripId = savedEntity.getTrip().getId();
                List<String> seatIdStrings = new ArrayList<>();
                UUID carId = null;

                for (BookingSeatDbModel bookingSeat : savedEntity.getBookingSeats()) {
                    if (!bookingSeat.getIsDeleted()) {
                        seatIdStrings.add(bookingSeat.getSeat().getId().toString());
                        if (carId == null) {
                            carId = bookingSeat.getSeat().getCar().getId();
                        }
                    }
                }

                if (!seatIdStrings.isEmpty() && carId != null) {
                    // Create SeatHoldRequest to release holds
                    org.example.bookvexebej2e.models.dto.seat.SeatHoldRequest releaseRequest = new org.example.bookvexebej2e.models.dto.seat.SeatHoldRequest();
                    releaseRequest.setTripId(tripId.toString());
                    releaseRequest.setCarId(carId.toString());
                    releaseRequest.setSeatIds(seatIdStrings);

                    // Release seat holds and broadcast update
                    boolean released = seatHoldService.releaseSeats(releaseRequest, "booking_confirmed", null);

                    // Also broadcast specific update for booking confirmation
                    seatHoldService.broadcastSeatUpdate(
                            tripId.toString(),
                            carId.toString(),
                            seatIdStrings,
                            "booked",
                            null,
                            "booking_confirmed");

                    log.info("Released {} seat holds for confirmed booking: {} (released: {})",
                            seatIdStrings.size(), savedEntity.getCode(), released);
                }
            }
        } catch (Exception e) {
            log.error("Error releasing seat holds for confirmed booking {}: {}", savedEntity.getId(), e.getMessage());
            // Don't fail the confirmation if seat hold release fails
        }

        // Send notification for confirmation
        try {
            UUID userId = userRepository.findByCustomerId(savedEntity.getCustomer()
                    .getId())
                    .orElseThrow().getId();
            String message = String.format("Đặt chỗ %s đã được xác nhận. Trạng thái: chờ khởi hành",
                    savedEntity.getCode());

            notificationService.sendNotification(
                    userId,
                    "TYPE_BOOKING_CONFIRMED",
                    "Xác nhận đặt chỗ",
                    message,
                    savedEntity.getId(),
                    savedEntity.getTrip().getId(),
                    "CHANNEL_BOOKING",
                    true,
                    true);
        } catch (Exception e) {
            log.warn("Failed to send notification for booking confirmation: {}", e.getMessage());
        }

        return bookingMapper.toResponse(savedEntity);
    }

    @Override
    public BookingResponse completeTrip(UUID id) {
        BookingDbModel entity = bookingRepository.findByIdAndNotDeleted(id)
                .orElse(null);

        if (entity == null) {
            throw new ResourceNotFoundException(BookingDbModel.class, id);
        }

        // Validate current status
        if (!BookingStatus.DEPARTING.equals(entity.getBookingStatus())) {
            throw new IllegalStateException("Trip can only be completed when status is 'departing'. Current status: "
                    + entity.getBookingStatus());
        }

        entity.setBookingStatus(BookingStatus.COMPLETED);
        BookingDbModel savedEntity = bookingRepository.save(entity);

        // Send notification for completion
        try {
            UUID userId = userRepository.findByCustomerId(savedEntity.getCustomer()
                    .getId())
                    .orElseThrow().getId();
            String message = String.format("Chuyến đi cho đặt chỗ %s đã hoàn thành. Cảm ơn bạn đã sử dụng dịch vụ!",
                    savedEntity.getCode());

            notificationService.sendNotification(
                    userId,
                    "TYPE_TRIP_COMPLETED",
                    "Chuyến đi hoàn thành",
                    message,
                    savedEntity.getId(),
                    savedEntity.getTrip().getId(),
                    "CHANNEL_BOOKING",
                    true,
                    true);
        } catch (Exception e) {
            log.warn("Failed to send notification for trip completion: {}", e.getMessage());
        }

        return bookingMapper.toResponse(savedEntity);
    }

    @Override
    public BookingResponse cancelBooking(UUID id) {
        BookingDbModel entity = bookingRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        entity.setBookingStatus(BookingStatus.CANCELLED);
        BookingDbModel savedEntity = bookingRepository.save(entity);

        // Send cancellation notification
        try {
            UUID userId = userRepository.findByCustomerId(savedEntity.getCustomer()
                    .getId())
                    .orElseThrow().getId();
            String message = String.format("Đặt chỗ %s đã được hủy.", savedEntity.getCode());

            notificationService.sendNotification(
                    userId,
                    "TYPE_BOOKING_CANCELLED",
                    "Hủy đặt chỗ",
                    message,
                    savedEntity.getId(),
                    savedEntity.getTrip().getId(),
                    "CHANNEL_BOOKING",
                    true,
                    true);
        } catch (Exception e) {
            log.warn("Failed to send notification for booking cancellation: {}", e.getMessage());
        }

        return bookingMapper.toResponse(savedEntity);
    }

    @Override
    public BookingResponse updateStatusByDate(UUID id) {
        BookingDbModel entity = bookingRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

        // Only update if status is AWAIT_GO
        if (!BookingStatus.AWAIT_GO.equals(entity.getBookingStatus())) {
            throw new IllegalStateException(
                    "Status can only be updated by date when current status is 'await_go'. Current status: "
                            + entity.getBookingStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureTime = entity.getTrip().getDepartureTime();

        if (departureTime != null) {
            if (now.isBefore(departureTime)) {
                // Departure date has not come yet, keep as await_go
                entity.setBookingStatus(BookingStatus.AWAIT_GO);
            } else {
                // Departure date has come, change to departing
                entity.setBookingStatus(BookingStatus.DEPARTING);
            }
        }

        BookingDbModel savedEntity = bookingRepository.save(entity);
        return bookingMapper.toResponse(savedEntity);
    }
}