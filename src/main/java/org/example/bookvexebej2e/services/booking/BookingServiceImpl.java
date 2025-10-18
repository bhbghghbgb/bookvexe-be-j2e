package org.example.bookvexebej2e.services.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.BookingMapper;
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

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final TripRepository tripRepository;
    private final TripStopRepository tripStopRepository;
    private final BookingSeatService bookingSeatService;
    private final BookingMapper bookingMapper;

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
        entity.setBookingStatus(createDto.getBookingStatus());
        entity.setTotalPrice(createDto.getTotalPrice());

        // Resolve relationships
        CustomerDbModel customer = customerRepository.findById(createDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(CustomerDbModel.class, createDto.getCustomerId()));
        entity.setCustomer(customer);

        TripDbModel trip = tripRepository.findById(createDto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, createDto.getTripId()));
        entity.setTrip(trip);

        TripStopDbModel pickupStop = tripStopRepository.findById(createDto.getPickupStopId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, createDto.getPickupStopId()));
        entity.setPickupStop(pickupStop);

        TripStopDbModel dropoffStop = tripStopRepository.findById(createDto.getDropoffStopId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, createDto.getDropoffStopId()));
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
        List<BookingDbModel> entities = bookingRepository.findAllNotDeleted();
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
}
