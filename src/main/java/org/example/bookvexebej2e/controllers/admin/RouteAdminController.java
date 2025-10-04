package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.requests.RouteQueryRequest;
import org.example.bookvexebej2e.services.admin.RouteAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/routes")
@Tag(name = "Route Admin", description = "Route management APIs for administrators")
public class RouteAdminController extends BaseAdminController<RouteDbModel, Integer, RouteQueryRequest> {

    private final RouteAdminService routeService;

    public RouteAdminController(RouteAdminService routeService) {
        this.routeService = routeService;
    }

    @Override
    protected BaseAdminService<RouteDbModel, Integer, RouteQueryRequest> getService() {
        return routeService;
    }
}
