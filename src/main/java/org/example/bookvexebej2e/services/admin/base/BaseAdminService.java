package org.example.bookvexebej2e.services.admin.base;

import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class BaseAdminService<T, ID, Q extends BasePageableQueryRequest> {

    /**
     * Bắt buộc class con phải cung cấp repository cụ thể
     * Repository phải extend JpaSpecificationExecutor để hỗ trợ dynamic query
     */
    protected abstract JpaRepository<T, ID> getRepository();

    /**
     * Xây dựng Specification từ Query Request
     * Class con override method này để custom filter logic
     */
    protected abstract Specification<T> buildSpecification(Q queryRequest);

    /**
     * Tìm kiếm với pagination và dynamic filters
     */
    public Page<T> findAll(Q queryRequest) {
        Pageable pageable = queryRequest.toPageable();
        Specification<T> spec = buildSpecification(queryRequest);

        if (getRepository() instanceof JpaSpecificationExecutor) {
            @SuppressWarnings("unchecked") JpaSpecificationExecutor<T> specExecutor =
                (JpaSpecificationExecutor<T>) getRepository();
            return specExecutor.findAll(spec, pageable);
        }

        // Fallback nếu repository không support Specification
        return getRepository().findAll(pageable);
    }

    /**
     * Tìm kiếm đơn giản không filter - cho backward compatibility
     */
    public Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    /**
     * Lấy theo ID
     */
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    /**
     * Lấy tất cả (không phân trang)
     */
    public List<T> findAll() {
        return getRepository().findAll();
    }

    /**
     * Kiểm tra tồn tại theo ID
     */
    public boolean existsById(ID id) {
        return getRepository().existsById(id);
    }

    /**
     * Lưu entity
     */
    public T save(T entity) {
        return getRepository().save(entity);
    }

    /**
     * Xóa theo ID
     */
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * Đếm tổng số bản ghi
     */
    public long count() {
        return getRepository().count();
    }
}
