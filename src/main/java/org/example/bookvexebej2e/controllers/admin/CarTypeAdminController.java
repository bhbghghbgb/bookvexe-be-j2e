package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.requests.CarTypeQueryRequest;
import org.example.bookvexebej2e.services.admin.CarTypeAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/car-types")
@Tag(name = "Car Type Admin", description = "Car type management APIs for administrators")
public class CarTypeAdminController extends BaseAdminController<CarTypeDbModel, Integer, CarTypeQueryRequest> {

    private final CarTypeAdminService carTypeService;

    public CarTypeAdminController(CarTypeAdminService carTypeService) {
        this.carTypeService = carTypeService;
    }

    @Override
    protected BaseAdminService<CarTypeDbModel, Integer, CarTypeQueryRequest> getService() {
        return carTypeService;
    }
}
