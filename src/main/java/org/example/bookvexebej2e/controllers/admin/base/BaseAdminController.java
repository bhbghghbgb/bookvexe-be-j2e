package org.example.bookvexebej2e.controllers.admin.base;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "keycloak")
@Tag(name = "Admin", description = "Admin management APIs")
public abstract class BaseAdminController<T, ID> {

    protected abstract BaseAdminService<T, ID> getService();

    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable ID id) {
        return getService().findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound()
                .build());
    }

    @GetMapping
    public ResponseEntity<Page<T>> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size, @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<T> result = getService().findAll(pageable);
        return ResponseEntity.ok(result);
    }
}
