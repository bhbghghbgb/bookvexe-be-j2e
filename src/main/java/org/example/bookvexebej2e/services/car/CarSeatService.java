package org.example.bookvexebej2e.services.car;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.car.CarSeatCreate;
import org.example.bookvexebej2e.models.dto.car.CarSeatQuery;
import org.example.bookvexebej2e.models.dto.car.CarSeatResponse;
import org.example.bookvexebej2e.models.dto.car.CarSeatSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarSeatUpdate;
import org.springframework.data.domain.Page;

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

    Page<CarSeatSelectResponse> findAllForSelect(CarSeatQuery query);

    // Method to automatically create car seats based on car type seat count
    void createSeatsForCar(UUID carId, Integer seatCount);

    // Method to delete all car seats for a specific car
    void deleteAllSeatsByCar(UUID carId);
}
