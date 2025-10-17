package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.requests.CarTypeCreateUpdateRequest;
import org.example.bookvexebej2e.models.requests.CarTypeQueryRequest;
import org.example.bookvexebej2e.services.admin.CarTypeAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Override pagination to accept JSON body
     */
    @PostMapping("/pagination")
    public ResponseEntity<Page<CarTypeDbModel>> pagination(@RequestBody CarTypeQueryRequest queryRequest) {
        Page<CarTypeDbModel> result = carTypeService.findAll(queryRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * Create a new car type
     */
    @PostMapping
    public ResponseEntity<CarTypeDbModel> create(@RequestBody CarTypeCreateUpdateRequest body) {
        CarTypeDbModel entity = new CarTypeDbModel();
        entity.setTypeName(body.getTypeName());
        entity.setDescription(body.getDescription());
        CarTypeDbModel saved = carTypeService.save(entity);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update an existing car type
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarTypeDbModel> update(@PathVariable Integer id, @RequestBody CarTypeCreateUpdateRequest body) {
        return carTypeService.findById(id)
            .map(existing -> {
                existing.setTypeName(body.getTypeName());
                existing.setDescription(body.getDescription());
                CarTypeDbModel updated = carTypeService.save(existing);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
