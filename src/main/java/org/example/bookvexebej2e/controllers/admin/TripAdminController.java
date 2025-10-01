package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.requests.TripQueryRequest;
import org.example.bookvexebej2e.services.admin.TripAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/trips")
@Tag(name = "Trip Admin", description = "Trip management APIs for administrators")
public class TripAdminController extends BaseAdminController<TripDbModel, Integer> {

    private final TripAdminService tripService;

    public TripAdminController(TripAdminService tripService) {
        this.tripService = tripService;
    }

    @Override
    protected BaseAdminService<TripDbModel, Integer> getService() {
        return tripService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TripDbModel>> searchTrips(@ModelAttribute TripQueryRequest queryRequest) {
        Page<TripDbModel> trips = tripService.findTripsByCriteria(queryRequest);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/future/available")
    public ResponseEntity<List<TripDbModel>> getFutureTripsWithAvailableSeats(@RequestParam(defaultValue = "1") Integer minSeats) {
        List<TripDbModel> trips = tripService.findFutureTripsWithAvailableSeats(minSeats);
        return ResponseEntity.ok(trips);
    }
}
