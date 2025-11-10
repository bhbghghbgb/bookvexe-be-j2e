package org.example.bookvexebej2e.services.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.BookingMapper;
import org.example.bookvexebej2e.models.constant.BookingStatus;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingSelectResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingUpdate;
import org.example.bookvexebej2e.helpers.api.PaymentApi;
import org.example.bookvexebej2e.helpers.dto.PaymentDto;
import org.example.bookvexebej2e.repositories.booking.BookingRepository;
import org.example.bookvexebej2e.repositories.customer.CustomerRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.trip.TripStopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import feign.FeignException;

/**
 * Booking Service Implementation
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final TripRepository tripRepository;
    private final TripStopRepository tripStopRepository;
    private final BookingSeatService bookingSeatService;
    private final BookingMapper bookingMapper;
    private final PaymentApi paymentApi;

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

        return bookingMapper.toResponse(savedEntity);
    }

    @Override
    public BookingResponse update(UUID id, BookingUpdate updateDto) {
        BookingDbModel entity = bookingRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, id));

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

        // Validate current status
        if (!BookingStatus.NEW.equals(entity.getBookingStatus())) {
            throw new IllegalStateException(
                    "Booking can only be confirmed when status is 'new'. Current status: " + entity.getBookingStatus());
        }

        // Check payment via Payment Service
        boolean isPaidSuccess = false;
        UUID paymentId = entity.getPaymentId();
        if (paymentId != null) {
            try {
                PaymentDto payment = paymentApi.findById(paymentId);
                isPaidSuccess = payment != null && "SUCCESS".equalsIgnoreCase(payment.getStatus());
            } catch (FeignException.NotFound ex) {
                isPaidSuccess = false;
            } catch (FeignException ex) {
                isPaidSuccess = false;
            }
        }

        if (isPaidSuccess) {
            entity.setBookingStatus(BookingStatus.AWAIT_GO);
        } else {
            entity.setBookingStatus(BookingStatus.AWAIT_PAYMENT);
        }

        BookingDbModel savedEntity = bookingRepository.save(entity);
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