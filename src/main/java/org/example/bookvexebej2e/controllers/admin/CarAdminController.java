package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.requests.CarQueryRequest;
import org.example.bookvexebej2e.services.admin.CarAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cars")
@Tag(name = "Car Admin", description = "Car management APIs for administrators")
public class CarAdminController extends BaseAdminController<CarDbModel, Integer, CarQueryRequest> {

    private final CarAdminService carService;

    public CarAdminController(CarAdminService carService) {
        this.carService = carService;
    }

    @Override
    protected BaseAdminService<CarDbModel, Integer, CarQueryRequest> getService() {
        return carService;
    }

    /**
     * Lấy danh sách ghế của xe
     */
    @GetMapping("/{carId}/seats")
    public ResponseEntity<List<CarSeatDbModel>> getCarSeats(@PathVariable Integer carId) {
        List<CarSeatDbModel> seats = carService.getCarSeats(carId);
        return ResponseEntity.ok(seats);
    }
}
