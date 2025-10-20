package org.example.bookvexebej2e.services.employee;

import org.example.bookvexebej2e.models.dto.employee.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    List<EmployeeResponse> findAll();

    Page<EmployeeResponse> findAll(EmployeeQuery query);

    EmployeeResponse findById(UUID id);

    EmployeeResponse create(EmployeeCreate createDto);

    EmployeeResponse update(UUID id, EmployeeUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<EmployeeSelectResponse> findAllForSelect();

    Page<EmployeeSelectResponse> findAllForSelect(EmployeeQuery query);
}
