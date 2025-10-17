package org.example.bookvexebej2e.service.car;

import org.example.bookvexebej2e.dto.car.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CarSeatService {
    List<CarSeatResponse> findAll();
    Page<CarSeatResponse> findAll(CarSeatQuery query);
    CarSeatResponse findById(UUID id);
    CarSeatResponse create(CarSeatCreate createDto);
    CarSeatResponse update(UUID id, CarSeatUpdate updateDto);
    void delete(UUID id);
    void activate(UUID id);
    void deactivate(UUID id);
    List<CarSeatSelectResponse> findAllForSelect();
}
