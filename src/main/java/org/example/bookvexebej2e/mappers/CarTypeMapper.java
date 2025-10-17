package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.dto.car.CarTypeCreate;
import org.example.bookvexebej2e.models.dto.car.CarTypeResponse;
import org.example.bookvexebej2e.models.dto.car.CarTypeSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarTypeUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarTypeMapper {

    CarTypeResponse toResponse(CarTypeDbModel entity);

    CarTypeSelectResponse toSelectResponse(CarTypeDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "cars", ignore = true)
    CarTypeDbModel toEntity(CarTypeCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "cars", ignore = true)
    void updateEntity(CarTypeUpdate updateDto, @MappingTarget CarTypeDbModel entity);
}
