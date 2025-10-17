package org.example.bookvexebej2e.service.car;

import org.example.bookvexebej2e.models.dto.car.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CarTypeService {
    List<CarTypeResponse> findAll();

    Page<CarTypeResponse> findAll(CarTypeQuery query);

    CarTypeResponse findById(UUID id);

    CarTypeResponse create(CarTypeCreate createDto);

    CarTypeResponse update(UUID id, CarTypeUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<CarTypeSelectResponse> findAllForSelect();
}
