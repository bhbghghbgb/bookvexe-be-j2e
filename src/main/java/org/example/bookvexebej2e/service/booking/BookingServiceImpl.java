package org.example.bookvexebej2e.service.booking;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.dto.booking.*;
import org.example.bookvexebej2e.mappers.BookingMapper;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.repository.booking.BookingRepository;
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
        BookingDbModel entity = bookingMapper.toEntity(createDto);
        BookingDbModel savedEntity = bookingRepository.save(entity);
        return bookingMapper.toResponse(savedEntity);
    }

    @Override
    public BookingResponse update(UUID id, BookingUpdate updateDto) {
        BookingDbModel entity = bookingRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        bookingMapper.updateEntity(updateDto, entity);
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
