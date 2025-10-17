package org.example.bookvexebej2e.services.invoice;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.InvoiceMapper;
import org.example.bookvexebej2e.models.db.InvoiceDbModel;
import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.dto.invoice.*;
import org.example.bookvexebej2e.repositories.invoice.InvoiceRepository;
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
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public List<InvoiceResponse> findAll() {
        List<InvoiceDbModel> entities = invoiceRepository.findAllNotDeleted();
        return entities.stream()
            .map(invoiceMapper::toResponse)
            .toList();
    }

    @Override
    public Page<InvoiceResponse> findAll(InvoiceQuery query) {
        Specification<InvoiceDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<InvoiceDbModel> entities = invoiceRepository.findAll(spec, pageable);
        return entities.map(invoiceMapper::toResponse);
    }

    @Override
    public InvoiceResponse findById(UUID id) {
        InvoiceDbModel entity = invoiceRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return invoiceMapper.toResponse(entity);
    }

    @Override
    public InvoiceResponse create(InvoiceCreate createDto) {
        InvoiceDbModel entity = new InvoiceDbModel();
        entity.setInvoiceNumber(createDto.getInvoiceNumber());
        entity.setFileUrl(createDto.getFileUrl());
        entity.setIssuedAt(createDto.getIssuedAt());

        PaymentDbModel payment = paymentRepository.findById(createDto.getPaymentId())
            .orElseThrow(() -> new RuntimeException("Payment not found with id: " + createDto.getPaymentId()));
        entity.setPayment(payment);

        InvoiceDbModel savedEntity = invoiceRepository.save(entity);
        return invoiceMapper.toResponse(savedEntity);
    }

    @Override
    public InvoiceResponse update(UUID id, InvoiceUpdate updateDto) {
        InvoiceDbModel entity = invoiceRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        entity.setInvoiceNumber(updateDto.getInvoiceNumber());
        entity.setFileUrl(updateDto.getFileUrl());
        entity.setIssuedAt(updateDto.getIssuedAt());

        if (updateDto.getPaymentId() != null) {
            PaymentDbModel payment = paymentRepository.findById(updateDto.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + updateDto.getPaymentId()));
            entity.setPayment(payment);
        }

        InvoiceDbModel updatedEntity = invoiceRepository.save(entity);
        return invoiceMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        invoiceRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        InvoiceDbModel entity = invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        entity.setIsDeleted(false);
        invoiceRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<InvoiceSelectResponse> findAllForSelect() {
        List<InvoiceDbModel> entities = invoiceRepository.findAllNotDeleted();
        return entities.stream()
            .map(invoiceMapper::toSelectResponse)
            .toList();
    }

    private Specification<InvoiceDbModel> buildSpecification(InvoiceQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getPaymentId() != null) {
                predicates.add(cb.equal(root.get("payment")
                    .get("id"), query.getPaymentId()));
            }
            if (query.getInvoiceNumber() != null && !query.getInvoiceNumber()
                .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("invoiceNumber")), "%" + query.getInvoiceNumber()
                    .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(InvoiceQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
