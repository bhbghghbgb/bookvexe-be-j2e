package org.example.bookvexebej2e.controllers.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.requests.TripQueryRequest;
import org.example.bookvexebej2e.services.user.TripUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
@Tag(name = "Trip Public", description = "Public trip search APIs for customers")
@CrossOrigin(origins = "*")
public class TripUserController {

    private final TripUserService tripUserService;

    public TripUserController(TripUserService tripUserService) {
        this.tripUserService = tripUserService;
    }

    /**
     * Public search with pagination and filters
     */
    @PostMapping("/pagination")
    public ResponseEntity<Page<TripDbModel>> search(@RequestBody TripQueryRequest queryRequest) {
        Page<TripDbModel> result = tripUserService.search(queryRequest);
        return ResponseEntity.ok(result);
    }
}
