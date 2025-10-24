package org.example.bookvexebej2e.services.route;

import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.RouteMapper;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.dto.route.RouteUserQuery;
import org.example.bookvexebej2e.models.dto.route.RouteUserResponse;
import org.example.bookvexebej2e.repositories.route.RouteUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteUserServiceImpl implements RouteUserService {

    private final RouteUserRepository routeRepository;
    private final RouteMapper routeMapper;

    /**
     * Lấy toàn bộ tuyến đường chưa bị xóa (isDeleted = false), sắp xếp theo
     * startLocation ASC
     */
    @Override
    public List<RouteUserResponse> findAll() {
        List<RouteDbModel> entities = routeRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "startLocation"));

        return entities.stream()
                .map(routeMapper::toUserResponse)
                .toList();
    }

    /**
     * Tìm kiếm tuyến đường theo query (lọc, phân trang, sắp xếp)
     */
    @Override
    public Page<RouteUserResponse> findAll(RouteUserQuery query) {
        Specification<RouteDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);

        Page<RouteDbModel> page = routeRepository.findAll(spec, pageable);
        return page.map(routeMapper::toUserResponse);
    }

    /**
     * Lấy chi tiết tuyến đường theo ID (chỉ lấy nếu chưa bị xóa)
     */
    @Override
    public RouteUserResponse findById(UUID id) {
        RouteDbModel entity = routeRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, id));
        return routeMapper.toUserResponse(entity);
    }

    /**
     * Danh sách điểm xuất phát duy nhất
     */
    @Override
    public List<RouteUserResponse> getAllStartLocations() {
        return routeRepository.findDistinctStartLocations()
                .stream()
                .map(RouteUserResponse::new)
                .toList();
    }

    /**
     * Danh sách điểm đến duy nhất
     */
    @Override
    public List<RouteUserResponse> getAllEndLocations() {
        return routeRepository.findDistinctEndLocations()
                .stream()
                .map(RouteUserResponse::new)
                .toList();
    }

    /**
     * Lấy danh sách điểm đến theo điểm xuất phát
     */
    @Override
    public List<RouteUserResponse> getEndLocationsByStartLocation(String startLocation) {
        return routeRepository.findDistinctEndLocationsByStartLocation(startLocation)
                .stream()
                .map(RouteUserResponse::new)
                .toList();
    }

    // ===================== PRIVATE HELPERS ===================== //

    /**
     * Tạo điều kiện lọc theo query (startLocation, endLocation, isDeleted = false)
     */
    private Specification<RouteDbModel> buildSpecification(RouteUserQuery query) {
        return (root, cq, cb) -> {
            var predicates = cb.conjunction();

            // Luôn lọc các route chưa bị xóa
            predicates.getExpressions().add(cb.isFalse(root.get("isDeleted")));

            if (query.getStartLocation() != null && !query.getStartLocation().isBlank()) {
                predicates.getExpressions().add(
                        cb.like(
                                cb.lower(root.get("startLocation")),
                                "%" + query.getStartLocation().trim().toLowerCase() + "%"));
            }

            if (query.getEndLocation() != null && !query.getEndLocation().isBlank()) {
                predicates.getExpressions().add(
                        cb.like(
                                cb.lower(root.get("endLocation")),
                                "%" + query.getEndLocation().trim().toLowerCase() + "%"));
            }

            return predicates;
        };
    }

    /**
     * Xây dựng cấu hình phân trang và sắp xếp
     */
    private Pageable buildPageable(RouteUserQuery query) {
        String sortBy = (query.getSortBy() != null && !query.getSortBy().isBlank())
                ? query.getSortBy()
                : "startLocation";

        Sort.Direction direction = "desc".equalsIgnoreCase(query.getSortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        int page = (query.getPage() != null && query.getPage() >= 0) ? query.getPage() : 0;
        int size = (query.getSize() != null && query.getSize() > 0) ? query.getSize() : 10;

        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
