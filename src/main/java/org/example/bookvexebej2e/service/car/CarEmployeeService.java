package org.example.bookvexebej2e.service.car;

import org.example.bookvexebej2e.dto.car.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CarEmployeeService {
    List<CarEmployeeResponse> findAll();

    Page<CarEmployeeResponse> findAll(CarEmployeeQuery query);

    CarEmployeeResponse findById(UUID id);

    CarEmployeeResponse create(CarEmployeeCreate createDto);

    CarEmployeeResponse update(UUID id, CarEmployeeUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<CarEmployeeSelectResponse> findAllForSelect();
}
