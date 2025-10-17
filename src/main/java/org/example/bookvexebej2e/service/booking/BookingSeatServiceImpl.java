package org.example.bookvexebej2e.service.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.mappers.BookingSeatMapper;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatQuery;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatSelectResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatUpdate;
import org.example.bookvexebej2e.repository.booking.BookingSeatRepository;
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
public class BookingSeatServiceImpl implements BookingSeatService {

    private final BookingSeatRepository bookingSeatRepository;
    private final BookingSeatMapper bookingSeatMapper;

    @Override
    public List<BookingSeatResponse> findAll() {
        List<BookingSeatDbModel> entities = bookingSeatRepository.findAllNotDeleted();
        return entities.stream()
                .map(bookingSeatMapper::toResponse)
                .toList();
    }

    @Override
    public Page<BookingSeatResponse> findAll(BookingSeatQuery query) {
        Specification<BookingSeatDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<BookingSeatDbModel> entities = bookingSeatRepository.findAll(spec, pageable);
        return entities.map(bookingSeatMapper::toResponse);
    }

    @Override
    public BookingSeatResponse findById(UUID id) {
        BookingSeatDbModel entity = bookingSeatRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("BookingSeat not found with id: " + id));
        return bookingSeatMapper.toResponse(entity);
    }

    @Override
    public BookingSeatResponse create(BookingSeatCreate createDto) {
        BookingSeatDbModel entity = bookingSeatMapper.toEntity(createDto);
        BookingSeatDbModel savedEntity = bookingSeatRepository.save(entity);
        return bookingSeatMapper.toResponse(savedEntity);
    }

    @Override
    public BookingSeatResponse update(UUID id, BookingSeatUpdate updateDto) {
        BookingSeatDbModel entity = bookingSeatRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("BookingSeat not found with id: " + id));
        bookingSeatMapper.updateEntity(updateDto, entity);
        BookingSeatDbModel updatedEntity = bookingSeatRepository.save(entity);
        return bookingSeatMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        bookingSeatRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        BookingSeatDbModel entity = bookingSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookingSeat not found with id: " + id));
        entity.setIsDeleted(false);
        bookingSeatRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<BookingSeatSelectResponse> findAllForSelect() {
        List<BookingSeatDbModel> entities = bookingSeatRepository.findAllNotDeleted();
        return entities.stream()
                .map(bookingSeatMapper::toSelectResponse)
                .toList();
    }

    private Specification<BookingSeatDbModel> buildSpecification(BookingSeatQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (query.getBookingId() != null) {
                predicates.add(cb.equal(root.get("booking")
                        .get("id"), query.getBookingId()));
            }
            if (query.getSeatId() != null) {
                predicates.add(cb.equal(root.get("seat")
                        .get("id"), query.getSeatId()));
            }
            if (query.getStatus() != null && !query.getStatus()
                    .isEmpty()) {
                predicates.add(cb.equal(root.get("status"), query.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(BookingSeatQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
