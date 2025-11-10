package org.example.bookvexebej2e.services.invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.InvoiceMapper;
import org.example.bookvexebej2e.models.db.InvoiceDbModel;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceCreate;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceQuery;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceSelectResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceUpdate;
import org.example.bookvexebej2e.repositories.invoice.InvoiceRepository;
import org.example.bookvexebej2e.helpers.api.PaymentApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final PaymentApi paymentApi;

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
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceDbModel.class, id));
        return invoiceMapper.toResponse(entity);
    }

    @Override
    public InvoiceResponse create(InvoiceCreate createDto) {
        InvoiceDbModel entity = new InvoiceDbModel();
        entity.setInvoiceNumber(createDto.getInvoiceNumber());
        entity.setFileUrl(createDto.getFileUrl());
        entity.setIssuedAt(createDto.getIssuedAt());
        if (createDto.getPaymentId() != null) {
            ensurePaymentExists(createDto.getPaymentId());
            entity.setPaymentId(createDto.getPaymentId());
        }

        InvoiceDbModel savedEntity = invoiceRepository.save(entity);
        return invoiceMapper.toResponse(savedEntity);
    }

    @Override
    public InvoiceResponse update(UUID id, InvoiceUpdate updateDto) {
        InvoiceDbModel entity = invoiceRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceDbModel.class, id));

        entity.setInvoiceNumber(updateDto.getInvoiceNumber());
        entity.setFileUrl(updateDto.getFileUrl());
        entity.setIssuedAt(updateDto.getIssuedAt());

        if (updateDto.getPaymentId() != null) {
            ensurePaymentExists(updateDto.getPaymentId());
            entity.setPaymentId(updateDto.getPaymentId());
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
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceDbModel.class, id));
        entity.setIsDeleted(false);
        invoiceRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        InvoiceDbModel entity = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceDbModel.class, id));
        entity.setIsDeleted(true);
        invoiceRepository.save(entity);
    }

    @Override
    public List<InvoiceSelectResponse> findAllForSelect() {
        List<InvoiceDbModel> entities = invoiceRepository.findAllNotDeleted();
        return entities.stream()
                .map(invoiceMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<InvoiceSelectResponse> findAllForSelect(InvoiceQuery query) {
        Specification<InvoiceDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<InvoiceDbModel> entities = invoiceRepository.findAll(spec, pageable);
        return entities.map(invoiceMapper::toSelectResponse);
    }

    private Specification<InvoiceDbModel> buildSpecification(InvoiceQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Remove the isDeleted filter to show all records including deleted ones

            if (query.getPaymentId() != null) {
                predicates.add(cb.equal(root.get("paymentId"), query.getPaymentId()));
            }
            if (query.getInvoiceNumber() != null && !query.getInvoiceNumber()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("invoiceNumber")), "%" + query.getInvoiceNumber()
                        .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void ensurePaymentExists(UUID paymentId) {
        try {
            paymentApi.findById(paymentId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found with id: " + paymentId);
        } //catch (HttpStatusCodeException ex) {
            // propagate as 502 Bad Gateway for upstream issues
            //throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to verify payment: " + ex.getMessage());
        //}
    }

    private Pageable buildPageable(InvoiceQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
