package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.requests.TripQueryRequest;
import org.example.bookvexebej2e.models.requests.TripCreateUpdateRequest;
import org.example.bookvexebej2e.services.admin.TripAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.example.bookvexebej2e.repositories.RouteRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/trips")
@Tag(name = "Trip Admin", description = "Trip management APIs for administrators")
public class TripAdminController extends BaseAdminController<TripDbModel, String, TripQueryRequest> {

    private final TripAdminService tripService;
    private final RouteRepository routeRepository;

    public TripAdminController(TripAdminService tripService, RouteRepository routeRepository) {
        this.tripService = tripService;
        this.routeRepository = routeRepository;
    }

    @Override
    protected BaseAdminService<TripDbModel, String, TripQueryRequest> getService() {
        return tripService;
    }

    /**
     * Override pagination to accept JSON body
     */
    @PostMapping("/pagination")
    public ResponseEntity<Page<TripDbModel>> pagination(@RequestBody TripQueryRequest queryRequest) {
        Page<TripDbModel> result = tripService.findAll(queryRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy các chuyến đi trong tương lai có ghế trống
     */
    @GetMapping("/future/available")
    public ResponseEntity<List<TripDbModel>> getFutureTripsWithAvailableSeats(
        @RequestParam(defaultValue = "1") Integer minSeats) {
        List<TripDbModel> trips = tripService.findFutureTripsWithAvailableSeats(minSeats);
        return ResponseEntity.ok(trips);
    }

    /**
     * Create a new trip
     */
    @PostMapping
    public ResponseEntity<TripDbModel> create(@RequestBody TripCreateUpdateRequest body) {
        TripDbModel entity = new TripDbModel();
        if (body.getRouteId() != null) {
            routeRepository.findById(body.getRouteId()).ifPresent(entity::setRoute);
        }
        entity.setDepartureTime(body.getDepartureTime());
        entity.setPrice(body.getPrice());
        entity.setAvailableSeats(body.getAvailableSeats());
        TripDbModel saved = tripService.save(entity);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update an existing trip
     */
    @PutMapping("/{id}")
    public ResponseEntity<TripDbModel> update(@PathVariable String id, @RequestBody TripCreateUpdateRequest body) {
        return tripService.findById(id)
            .map(existing -> {
                if (body.getRouteId() != null) {
                    routeRepository.findById(body.getRouteId()).ifPresent(existing::setRoute);
                }
                existing.setDepartureTime(body.getDepartureTime());
                existing.setPrice(body.getPrice());
                existing.setAvailableSeats(body.getAvailableSeats());
                TripDbModel updated = tripService.save(existing);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
