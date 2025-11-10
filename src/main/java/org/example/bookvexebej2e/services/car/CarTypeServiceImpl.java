package org.example.bookvexebej2e.services.car;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.CarTypeMapper;
import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.dto.car.CarTypeCreate;
import org.example.bookvexebej2e.models.dto.car.CarTypeQuery;
import org.example.bookvexebej2e.models.dto.car.CarTypeResponse;
import org.example.bookvexebej2e.models.dto.car.CarTypeSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarTypeUpdate;
import org.example.bookvexebej2e.repositories.car.CarTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarTypeServiceImpl implements CarTypeService {

    private final CarTypeRepository carTypeRepository;
    private final CarTypeMapper carTypeMapper;

    @Override
    public List<CarTypeResponse> findAll() {
        List<CarTypeDbModel> entities = carTypeRepository.findAllNotDeleted();
        return entities.stream()
                .map(carTypeMapper::toResponse)
                .toList();
    }

    @Override
    public Page<CarTypeResponse> findAll(CarTypeQuery query) {
        Specification<CarTypeDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarTypeDbModel> entities = carTypeRepository.findAll(spec, pageable);
        return entities.map(carTypeMapper::toResponse);
    }

    @Override
    public CarTypeResponse findById(UUID id) {
        CarTypeDbModel entity = carTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarTypeDbModel.class, id));
        return carTypeMapper.toResponse(entity);
    }

    @Override
    public CarTypeResponse create(CarTypeCreate createDto) {
        // Check for duplicate code
        if (createDto.getCode() != null && !createDto.getCode().isEmpty()) {
            carTypeRepository.findByCode(createDto.getCode()).ifPresent(existingCarType -> {
                throw new IllegalStateException("Không thể tạo loại xe vì mã loại xe '" + createDto.getCode() + "' đã tồn tại trong hệ thống");
            });
        }

        CarTypeDbModel entity = new CarTypeDbModel();
        entity.setCode(createDto.getCode());
        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());
        entity.setSeatCount(createDto.getSeatCount());

        CarTypeDbModel savedEntity = carTypeRepository.save(entity);
        return carTypeMapper.toResponse(savedEntity);
    }

    @Override
    public CarTypeResponse update(UUID id, CarTypeUpdate updateDto) {
        CarTypeDbModel entity = carTypeRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarTypeDbModel.class, id));

        // Check for duplicate code (excluding current entity)
        if (updateDto.getCode() != null && !updateDto.getCode().isEmpty()) {
            carTypeRepository.findByCode(updateDto.getCode()).ifPresent(existingCarType -> {
                if (!existingCarType.getId().equals(id)) {
                    throw new IllegalStateException("Không thể cập nhật loại xe vì mã loại xe '" + updateDto.getCode() + "' đã tồn tại trong hệ thống");
                }
            });
        }

        entity.setCode(updateDto.getCode());
        entity.setName(updateDto.getName());
        entity.setDescription(updateDto.getDescription());
        entity.setSeatCount(updateDto.getSeatCount());

        CarTypeDbModel updatedEntity = carTypeRepository.save(entity);
        return carTypeMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        carTypeRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        CarTypeDbModel entity = carTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarTypeDbModel.class, id));
        entity.setIsDeleted(false);
        carTypeRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        CarTypeDbModel entity = carTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarTypeDbModel.class, id));
        entity.setIsDeleted(true);
        carTypeRepository.save(entity);
    }

    @Override
    public List<CarTypeSelectResponse> findAllForSelect() {
        List<CarTypeDbModel> entities = carTypeRepository.findAllNotDeleted();
        return entities.stream()
                .map(carTypeMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<CarTypeSelectResponse> findAllForSelect(CarTypeQuery query) {
        Specification<CarTypeDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarTypeDbModel> entities = carTypeRepository.findAll(spec, pageable);
        return entities.map(carTypeMapper::toSelectResponse);
    }

    private Specification<CarTypeDbModel> buildSpecification(CarTypeQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Remove the isDeleted filter to show all records including deleted ones

            // Thử với tên cột chính xác - có thể là "is_deleted" hoặc "isDeleted"
            if (query.getIsDeleted() != null) predicates.add(cb.equal(root.get("isDeleted"), query.getIsDeleted()));
            if (query.getCode() != null && !query.getCode()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + query.getCode()
                        .toLowerCase() + "%"));
            }
            if (query.getName() != null && !query.getName()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.getName()
                        .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(CarTypeQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}