package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.requests.TripQueryRequest;
import org.example.bookvexebej2e.services.admin.TripAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/trips")
@Tag(name = "Trip Admin", description = "Trip management APIs for administrators")
public class TripAdminController extends BaseAdminController<TripDbModel, Integer, TripQueryRequest> {

    private final TripAdminService tripService;

    public TripAdminController(TripAdminService tripService) {
        this.tripService = tripService;
    }

    @Override
    protected BaseAdminService<TripDbModel, Integer, TripQueryRequest> getService() {
        return tripService;
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
}
