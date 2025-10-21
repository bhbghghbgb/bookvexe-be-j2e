package org.example.bookvexebej2e.services.payment;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.PaymentMethodMapper;
import org.example.bookvexebej2e.models.db.PaymentMethodDbModel;
import org.example.bookvexebej2e.models.dto.payment.*;
import org.example.bookvexebej2e.repositories.payment.PaymentMethodRepository;
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
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @Override
    public List<PaymentMethodResponse> findAll() {
        List<PaymentMethodDbModel> entities = paymentMethodRepository.findAllNotDeleted();
        return entities.stream()
            .map(paymentMethodMapper::toResponse)
            .toList();
    }

    @Override
    public Page<PaymentMethodResponse> findAll(PaymentMethodQuery query) {
        Specification<PaymentMethodDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<PaymentMethodDbModel> entities = paymentMethodRepository.findAll(spec, pageable);
        return entities.map(paymentMethodMapper::toResponse);
    }

    @Override
    public PaymentMethodResponse findById(UUID id) {
        PaymentMethodDbModel entity = paymentMethodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(PaymentMethodDbModel.class, id));
        return paymentMethodMapper.toResponse(entity);
    }

    @Override
    public PaymentMethodResponse create(PaymentMethodCreate createDto) {
        PaymentMethodDbModel entity = new PaymentMethodDbModel();
        entity.setCode(createDto.getCode());
        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());
        entity.setIsDeleted(createDto.getIsDeleted());

        PaymentMethodDbModel savedEntity = paymentMethodRepository.save(entity);
        return paymentMethodMapper.toResponse(savedEntity);
    }

    @Override
    public PaymentMethodResponse update(UUID id, PaymentMethodUpdate updateDto) {
        PaymentMethodDbModel entity = paymentMethodRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(PaymentMethodDbModel.class, id));

        entity.setCode(updateDto.getCode());
        entity.setName(updateDto.getName());
        entity.setDescription(updateDto.getDescription());
        entity.setIsDeleted(updateDto.getIsDeleted());

        PaymentMethodDbModel updatedEntity = paymentMethodRepository.save(entity);
        return paymentMethodMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        paymentMethodRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        PaymentMethodDbModel entity = paymentMethodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(PaymentMethodDbModel.class, id));
        entity.setIsDeleted(false);
        paymentMethodRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<PaymentMethodSelectResponse> findAllForSelect() {
        List<PaymentMethodDbModel> entities = paymentMethodRepository.findAllNotDeleted();
        return entities.stream()
            .map(paymentMethodMapper::toSelectResponse)
            .toList();
    }

    @Override
    public Page<PaymentMethodSelectResponse> findAllForSelect(PaymentMethodQuery query) {
        Specification<PaymentMethodDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<PaymentMethodDbModel> entities = paymentMethodRepository.findAll(spec, pageable);
        return entities.map(paymentMethodMapper::toSelectResponse);
    }


    private Specification<PaymentMethodDbModel> buildSpecification(PaymentMethodQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getCode() != null && !query.getCode()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + query.getCode()
                    .toLowerCase() + "%"));
            }
            if (query.getName() != null && !query.getName()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.getName()
                    .toLowerCase() + "%"));
            }
            if (query.getIsDeleted() != null) {
                predicates.add(cb.equal(root.get("isDeleted"), query.getIsDeleted()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(PaymentMethodQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
