package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.requests.CarSeatQueryRequest;
import org.example.bookvexebej2e.services.admin.CarSeatAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/car-seats")
@Tag(name = "Car Seat Admin", description = "Car seat management APIs for administrators")
public class CarSeatAdminController extends BaseAdminController<CarSeatDbModel, Integer, CarSeatQueryRequest> {

    private final CarSeatAdminService carSeatService;

    public CarSeatAdminController(CarSeatAdminService carSeatService) {
        this.carSeatService = carSeatService;
    }

    @Override
    protected BaseAdminService<CarSeatDbModel, Integer, CarSeatQueryRequest> getService() {
        return carSeatService;
    }
}
