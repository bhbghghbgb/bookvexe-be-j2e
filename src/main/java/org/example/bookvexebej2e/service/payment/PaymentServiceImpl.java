package org.example.bookvexebej2e.service.payment;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.PaymentMapper;
import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.dto.payment.*;
import org.example.bookvexebej2e.repository.payment.PaymentRepository;
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
    private final PaymentMapper paymentMapper;

    @Override
    public List<PaymentResponse> findAll() {
        List<PaymentDbModel> entities = paymentRepository.findAllByIsDeletedFalse();
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
        PaymentDbModel entity = paymentRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return paymentMapper.toResponse(entity);
    }

    @Override
    public PaymentResponse create(PaymentCreate createDto) {
        PaymentDbModel entity = paymentMapper.toEntity(createDto);
        PaymentDbModel savedEntity = paymentRepository.save(entity);
        return paymentMapper.toResponse(savedEntity);
    }

    @Override
    public PaymentResponse update(UUID id, PaymentUpdate updateDto) {
        PaymentDbModel entity = paymentRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        paymentMapper.updateEntity(updateDto, entity);
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
            .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        entity.setIsDeleted(false);
        paymentRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<PaymentSelectResponse> findAllForSelect() {
        List<PaymentDbModel> entities = paymentRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(paymentMapper::toSelectResponse)
            .toList();
    }

    private Specification<PaymentDbModel> buildSpecification(PaymentQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

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
