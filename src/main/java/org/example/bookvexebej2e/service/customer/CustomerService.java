package org.example.bookvexebej2e.service.customer;

import org.example.bookvexebej2e.dto.customer.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<CustomerResponse> findAll();
    Page<CustomerResponse> findAll(CustomerQuery query);
    CustomerResponse findById(UUID id);
    CustomerResponse create(CustomerCreate createDto);
    CustomerResponse update(UUID id, CustomerUpdate updateDto);
    void delete(UUID id);
    void activate(UUID id);
    void deactivate(UUID id);
    List<CustomerSelectResponse> findAllForSelect();
}
