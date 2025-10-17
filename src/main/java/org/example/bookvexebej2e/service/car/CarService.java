package org.example.bookvexebej2e.service.car;

import org.example.bookvexebej2e.models.dto.car.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CarService {
    List<CarResponse> findAll();

    Page<CarResponse> findAll(CarQuery query);

    CarResponse findById(UUID id);

    CarResponse create(CarCreate createDto);

    CarResponse update(UUID id, CarUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<CarSelectResponse> findAllForSelect();
}
