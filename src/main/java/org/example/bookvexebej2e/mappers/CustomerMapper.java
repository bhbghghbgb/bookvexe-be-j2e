package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.dto.customer.CustomerCreate;
import org.example.bookvexebej2e.models.dto.customer.CustomerResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerSelectResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toResponse(CustomerDbModel entity);

    CustomerSelectResponse toSelectResponse(CustomerDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "user", ignore = true)
    CustomerDbModel toEntity(CustomerCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "user", ignore = true)
    void updateEntity(CustomerUpdate updateDto, @MappingTarget CustomerDbModel entity);
}
