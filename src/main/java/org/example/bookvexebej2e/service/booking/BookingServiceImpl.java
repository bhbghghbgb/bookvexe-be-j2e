package org.example.bookvexebej2e.service.booking;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.BookingMapper;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.booking.*;
import org.example.bookvexebej2e.repository.booking.BookingRepository;
import org.example.bookvexebej2e.repository.trip.TripRepository;
import org.example.bookvexebej2e.repository.trip.TripStopRepository;
import org.example.bookvexebej2e.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final TripStopRepository tripStopRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingResponse> findAll() {
        List<BookingDbModel> entities = bookingRepository.findAllByIsDeletedFalse();
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
        BookingDbModel entity = bookingRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
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
        UserDbModel user = userRepository.findById(createDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + createDto.getUserId()));
        entity.setUser(user);

        TripDbModel trip = tripRepository.findById(createDto.getTripId())
            .orElseThrow(() -> new RuntimeException("Trip not found with id: " + createDto.getTripId()));
        entity.setTrip(trip);

        TripStopDbModel pickupStop = tripStopRepository.findById(createDto.getPickupStopId())
            .orElseThrow(() -> new RuntimeException("TripStop not found with id: " + createDto.getPickupStopId()));
        entity.setPickupStop(pickupStop);

        TripStopDbModel dropoffStop = tripStopRepository.findById(createDto.getDropoffStopId())
            .orElseThrow(() -> new RuntimeException("TripStop not found with id: " + createDto.getDropoffStopId()));
        entity.setDropoffStop(dropoffStop);

        BookingDbModel savedEntity = bookingRepository.save(entity);
        return bookingMapper.toResponse(savedEntity);
    }

    @Override
    public BookingResponse update(UUID id, BookingUpdate updateDto) {
        BookingDbModel entity = bookingRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        entity.setCode(updateDto.getCode());
        entity.setType(updateDto.getType());
        entity.setBookingStatus(updateDto.getBookingStatus());
        entity.setTotalPrice(updateDto.getTotalPrice());

        // Resolve relationships if provided
        if (updateDto.getUserId() != null) {
            UserDbModel user = userRepository.findById(updateDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + updateDto.getUserId()));
            entity.setUser(user);
        }

        if (updateDto.getTripId() != null) {
            TripDbModel trip = tripRepository.findById(updateDto.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + updateDto.getTripId()));
            entity.setTrip(trip);
        }

        if (updateDto.getPickupStopId() != null) {
            TripStopDbModel pickupStop = tripStopRepository.findById(updateDto.getPickupStopId())
                .orElseThrow(() -> new RuntimeException("TripStop not found with id: " + updateDto.getPickupStopId()));
            entity.setPickupStop(pickupStop);
        }

        if (updateDto.getDropoffStopId() != null) {
            TripStopDbModel dropoffStop = tripStopRepository.findById(updateDto.getDropoffStopId())
                .orElseThrow(() -> new RuntimeException("TripStop not found with id: " + updateDto.getDropoffStopId()));
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
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        entity.setIsDeleted(false);
        bookingRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<BookingSelectResponse> findAllForSelect() {
        List<BookingDbModel> entities = bookingRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(bookingMapper::toSelectResponse)
            .toList();
    }

    private Specification<BookingDbModel> buildSpecification(BookingQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

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
            if (query.getUserId() != null) {
                predicates.add(cb.equal(root.get("user")
                    .get("id"), query.getUserId()));
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
