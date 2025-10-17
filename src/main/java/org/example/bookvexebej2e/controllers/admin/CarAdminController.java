package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.requests.CarQueryRequest;
import org.example.bookvexebej2e.models.requests.CarCreateUpdateRequest;
import org.example.bookvexebej2e.services.admin.CarAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.example.bookvexebej2e.repositories.CarTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cars")
@Tag(name = "Car Admin", description = "Car management APIs for administrators")
public class CarAdminController extends BaseAdminController<CarDbModel, Integer, CarQueryRequest> {

    private final CarAdminService carService;
    private final CarTypeRepository carTypeRepository;

    public CarAdminController(CarAdminService carService, CarTypeRepository carTypeRepository) {
        this.carService = carService;
        this.carTypeRepository = carTypeRepository;
    }

    @Override
    protected BaseAdminService<CarDbModel, Integer, CarQueryRequest> getService() {
        return carService;
    }

    /**
     * Override pagination to accept JSON body
     */
    @PostMapping("/pagination")
    public ResponseEntity<Page<CarDbModel>> pagination(@RequestBody CarQueryRequest queryRequest) {
        Page<CarDbModel> result = carService.findAll(queryRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách ghế của xe
     */
    @GetMapping("/{carId}/seats")
    public ResponseEntity<List<CarSeatDbModel>> getCarSeats(@PathVariable Integer carId) {
        List<CarSeatDbModel> seats = carService.getCarSeats(carId);
        return ResponseEntity.ok(seats);
    }

    /**
     * Create a new car
     */
    @PostMapping
    public ResponseEntity<CarDbModel> create(@RequestBody CarCreateUpdateRequest body) {
        CarDbModel car = new CarDbModel();
        if (body.getCarTypeId() != null) {
            carTypeRepository.findById(body.getCarTypeId()).ifPresent(car::setCarType);
        }
        car.setLicensePlate(body.getLicensePlate());
        car.setSeatCount(body.getSeatCount());
        car.setIsActive(Boolean.TRUE);
        CarDbModel saved = carService.save(car);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update an existing car
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarDbModel> update(@PathVariable Integer id, @RequestBody CarCreateUpdateRequest body) {
        return carService.findById(id)
            .map(existing -> {
                if (body.getCarTypeId() != null) {
                    carTypeRepository.findById(body.getCarTypeId()).ifPresent(existing::setCarType);
                }
                existing.setLicensePlate(body.getLicensePlate());
                existing.setSeatCount(body.getSeatCount());
                CarDbModel updated = carService.save(existing);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
