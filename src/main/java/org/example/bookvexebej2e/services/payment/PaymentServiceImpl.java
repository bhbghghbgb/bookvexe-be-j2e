package org.example.bookvexebej2e.services.payment;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.PaymentMapper;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.db.PaymentMethodDbModel;
import org.example.bookvexebej2e.models.dto.payment.*;
import org.example.bookvexebej2e.repositories.booking.BookingRepository;
import org.example.bookvexebej2e.repositories.payment.PaymentMethodRepository;
import org.example.bookvexebej2e.repositories.payment.PaymentRepository;
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
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public List<PaymentResponse> findAll() {
        List<PaymentDbModel> entities = paymentRepository.findAllNotDeleted();
        return entities.stream()
            .map(paymentMapper::toResponse)
            .toList();
    }

    @Override
    public Page<PaymentResponse> findAll(PaymentQuery query) {
        Specification<PaymentDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<PaymentDbModel> entities = paymentRepository.findAll(spec, pageable);
        return entities.map(paymentMapper::toResponse);
    }

    @Override
    public PaymentResponse findById(UUID id) {
        PaymentDbModel entity = paymentRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(PaymentDbModel.class, id));
        return paymentMapper.toResponse(entity);
    }

    @Override
    public PaymentResponse create(PaymentCreate createDto) {
        PaymentDbModel entity = new PaymentDbModel();
        entity.setAmount(createDto.getAmount());
        entity.setStatus(createDto.getStatus());
        entity.setTransactionCode(createDto.getTransactionCode());
        entity.setPaidAt(createDto.getPaidAt());

        BookingDbModel booking = bookingRepository.findById(createDto.getBookingId())
            .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, createDto.getBookingId()));
        entity.setBooking(booking);

        PaymentMethodDbModel method = paymentMethodRepository.findById(createDto.getMethodId())
            .orElseThrow(() -> new ResourceNotFoundException(PaymentMethodDbModel.class, createDto.getMethodId()));
        entity.setMethod(method);

        PaymentDbModel savedEntity = paymentRepository.save(entity);
        return paymentMapper.toResponse(savedEntity);
    }

    @Override
    public PaymentResponse update(UUID id, PaymentUpdate updateDto) {
        PaymentDbModel entity = paymentRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(PaymentDbModel.class, id));

        entity.setAmount(updateDto.getAmount());
        entity.setStatus(updateDto.getStatus());
        entity.setTransactionCode(updateDto.getTransactionCode());
        entity.setPaidAt(updateDto.getPaidAt());

        if (updateDto.getBookingId() != null) {
            BookingDbModel booking = bookingRepository.findById(updateDto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, updateDto.getBookingId()));
            entity.setBooking(booking);
        }

        if (updateDto.getMethodId() != null) {
            PaymentMethodDbModel method = paymentMethodRepository.findById(updateDto.getMethodId())
                .orElseThrow(() -> new ResourceNotFoundException(PaymentMethodDbModel.class, updateDto.getMethodId()));
            entity.setMethod(method);
        }

        PaymentDbModel updatedEntity = paymentRepository.save(entity);
        return paymentMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        paymentRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        PaymentDbModel entity = paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(PaymentDbModel.class, id));
        entity.setIsDeleted(false);
        paymentRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<PaymentSelectResponse> findAllForSelect() {
        List<PaymentDbModel> entities = paymentRepository.findAllNotDeleted();
        return entities.stream()
            .map(paymentMapper::toSelectResponse)
            .toList();
    }

    @Override
    public Page<PaymentSelectResponse> findAllForSelect(PaymentQuery query) {
        Specification<PaymentDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<PaymentDbModel> entities = paymentRepository.findAll(spec, pageable);
        return entities.map(paymentMapper::toSelectResponse);
    }


    private Specification<PaymentDbModel> buildSpecification(PaymentQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getBookingId() != null) {
                predicates.add(cb.equal(root.get("booking")
                    .get("id"), query.getBookingId()));
            }
            if (query.getMethodId() != null) {
                predicates.add(cb.equal(root.get("method")
                    .get("id"), query.getMethodId()));
            }
            if (query.getStatus() != null && !query.getStatus()
                .isEmpty()) {
                predicates.add(cb.equal(root.get("status"), query.getStatus()));
            }
            if (query.getTransactionCode() != null && !query.getTransactionCode()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("transactionCode")), "%" + query.getTransactionCode()
                    .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(PaymentQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
