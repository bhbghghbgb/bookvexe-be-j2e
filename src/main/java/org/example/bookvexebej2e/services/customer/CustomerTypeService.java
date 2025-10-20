package org.example.bookvexebej2e.services.customer;

import org.example.bookvexebej2e.models.dto.customer.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CustomerTypeService {
    List<CustomerTypeResponse> findAll();

    Page<CustomerTypeResponse> findAll(CustomerTypeQuery query);

    CustomerTypeResponse findById(UUID id);

    CustomerTypeResponse create(CustomerTypeCreate createDto);

    CustomerTypeResponse update(UUID id, CustomerTypeUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<CustomerTypeSelectResponse> findAllForSelect();

    Page<CustomerTypeSelectResponse> findAllForSelect(CustomerTypeQuery query);
}
