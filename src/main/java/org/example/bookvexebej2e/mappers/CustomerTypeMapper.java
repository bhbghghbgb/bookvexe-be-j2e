package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.customer.CustomerTypeCreate;
import org.example.bookvexebej2e.dto.customer.CustomerTypeResponse;
import org.example.bookvexebej2e.dto.customer.CustomerTypeSelectResponse;
import org.example.bookvexebej2e.dto.customer.CustomerTypeUpdate;
import org.example.bookvexebej2e.models.db.CustomerTypeDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerTypeMapper {

    CustomerTypeResponse toResponse(CustomerTypeDbModel entity);

    CustomerTypeSelectResponse toSelectResponse(CustomerTypeDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "customers", ignore = true)
    CustomerTypeDbModel toEntity(CustomerTypeCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "customers", ignore = true)
    void updateEntity(CustomerTypeUpdate updateDto, @MappingTarget CustomerTypeDbModel entity);
}
