package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.requests.RouteQueryRequest;
import org.example.bookvexebej2e.services.admin.RouteAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/routes")
@Tag(name = "Route Admin", description = "Route management APIs for administrators")
public class RouteAdminController extends BaseAdminController<RouteDbModel, Integer> {

    private final RouteAdminService routeService;

    public RouteAdminController(RouteAdminService routeService) {
        this.routeService = routeService;
    }

    @Override
    protected BaseAdminService<RouteDbModel, Integer> getService() {
        return routeService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RouteDbModel>> searchRoutes(@ModelAttribute RouteQueryRequest queryRequest) {
        Page<RouteDbModel> routes = routeService.findRoutesByCriteria(queryRequest);
        return ResponseEntity.ok(routes);
    }
}
