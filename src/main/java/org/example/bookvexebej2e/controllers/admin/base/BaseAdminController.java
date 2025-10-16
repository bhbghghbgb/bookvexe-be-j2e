package org.example.bookvexebej2e.controllers.admin.base;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Base Admin Controller với generic pagination support
 *
 * @param <T>  Entity type
 * @param <ID> ID type
 * @param <Q>  Query Request type extends BasePageableQueryRequest
 */
@Tag(name = "Admin", description = "Admin management APIs")
public abstract class BaseAdminController<T, ID, Q extends BasePageableQueryRequest> {

    /**
     * Class con phải cung cấp service tương ứng
     */
    protected abstract BaseAdminService<T, ID, Q> getService();

    /**
     * Lấy entity theo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get entity by ID")
    public ResponseEntity<T> findById(@PathVariable ID id) {
        return getService().findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound()
                .build());
    }

    /**
     * Pagination với dynamic filtering
     * Endpoint này sẽ nhận QueryRequest cụ thể của từng entity
     * <p>
     * Example request body:
     * {
     * "page": 0,
     * "size": 20,
     * "sortBy": "createdAt",
     * "sortDirection": "DESC",
     * ... other filters specific to entity
     * }
     */
    @GetMapping("/pagination")
    @Operation(summary = "Search with pagination and filters")
    public ResponseEntity<Page<T>> pagination(@ModelAttribute Q queryRequest) {
        Page<T> result = getService().findAll(queryRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @Operation(summary = "Get all entities (without pagination)")
    public ResponseEntity<List<T>> findAll() {
        List<T> result = getService().findAll();
        return ResponseEntity.ok(result);
    }

    // CO DTO RIENG CHO CREATE/UPDATE, KHONG GENERALIZE HET DUOC !!!
    //
    //    /**
    //     * Tạo mới entity
    //     */
    //    @PostMapping
    //    @Operation(summary = "Create new entity")
    //    public ResponseEntity<T> create(@RequestBody T entity) {
    //        T saved = getService().save(entity);
    //        return ResponseEntity.ok(saved);
    //    }
    //
    //    /**
    //     * Cập nhật entity
    //     */
    //    @PutMapping("/{id}")
    //    @Operation(summary = "Update entity")
    //    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
    //        if (!getService().existsById(id)) {
    //            return ResponseEntity.notFound()
    //                .build();
    //        }
    //        T updated = getService().save(entity);
    //        return ResponseEntity.ok(updated);
    //    }

    /**
     * Xóa entity
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete entity")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        if (!getService().existsById(id)) {
            return ResponseEntity.notFound()
                .build();
        }
        getService().deleteById(id);
        return ResponseEntity.noContent()
            .build();
    }
}
