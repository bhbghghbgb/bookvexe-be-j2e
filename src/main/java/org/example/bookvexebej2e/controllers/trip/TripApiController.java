package org.example.bookvexebej2e.controllers.trip;

import org.example.bookvexebej2e.models.dto.trip.TripQuery;
import org.example.bookvexebej2e.models.dto.trip.TripResponse;
import org.example.bookvexebej2e.services.trip.TripService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * API Controller for Chat Service
 * Provides public endpoints for trip information
 */
@RestController
@RequestMapping("/api/v1/trips")
public class TripApiController {

    private final TripService tripService;

    public TripApiController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Count total trips
     * GET /api/v1/trips/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        TripQuery query = new TripQuery();
        query.setIsDeleted(false);
        Page<TripResponse> page = tripService.findAll(query);
        return ResponseEntity.ok(page.getTotalElements());
    }

    /**
     * Get all trips with pagination
     * GET /api/v1/trips?page=0&size=20
     */
    @GetMapping
    public ResponseEntity<Page<TripResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "departureTime") String sort
    ) {
        TripQuery query = new TripQuery();
        query.setPage(page);
        query.setSize(size);
        query.setSortBy(sort);
        query.setIsDeleted(false);
        return ResponseEntity.ok(tripService.findAll(query));
    }

    /**
     * Search trips by criteria
     * POST /api/v1/trips/search
     */
    @PostMapping("/search")
    public ResponseEntity<List<TripResponse>> search(@RequestBody TripSearchRequest request) {
        TripQuery query = buildQueryFromRequest(request);
        Page<TripResponse> page = tripService.findAll(query);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Search trips with pagination
     * POST /api/v1/trips/search/page
     */
    @PostMapping("/search/page")
    public ResponseEntity<Page<TripResponse>> searchWithPage(@RequestBody TripSearchRequest request) {
        TripQuery query = buildQueryFromRequest(request);
        query.setPage(request.getPage() != null ? request.getPage() : 0);
        query.setSize(request.getSize() != null ? request.getSize() : 20);
        query.setSortBy(request.getSort() != null ? request.getSort() : "departureTime");
        
        return ResponseEntity.ok(tripService.findAll(query));
    }

    /**
     * Helper method to build TripQuery from TripSearchRequest
     */
    private TripQuery buildQueryFromRequest(TripSearchRequest request) {
        TripQuery query = new TripQuery();
        query.setRouteId(request.getRouteId());
        query.setIsDeleted(request.getIsDeleted() != null ? request.getIsDeleted() : false);
        
        // Handle date range filtering
        query.setDepartureTimeFrom(request.getDepartureTimeFrom());
        query.setDepartureTimeTo(request.getDepartureTimeTo());
        
        return query;
    }

    /**
     * DTO for trip search request
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TripSearchRequest {
        private UUID routeId;
        private LocalDateTime departureTimeFrom;
        private LocalDateTime departureTimeTo;
        private Boolean isDeleted;
        private Integer page;
        private Integer size;
        private String sort;
    }
}
